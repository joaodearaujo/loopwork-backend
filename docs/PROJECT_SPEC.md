# Loopwork — Formal Project Documentation

## 1. Overview

**Problem:** Self-employed professionals with recurring clients (personal
trainers, therapists, private tutors, nutritionists, etc.) currently manage
their schedules via WhatsApp or paper. This creates two recurring problems:

- Scheduling conflicts — the professional forgets an existing appointment
  and reschedules informally, causing confusion.
- No-shows without warning — the client forgets the session and the
  professional only finds out at the time, with no automatic reminder.

**Solution:** a scheduling system designed around **recurring sessions**
(e.g., "Client X, every Tuesday at 6 PM"), with support for one-off
exceptions (cancelling or rescheduling a single occurrence without
affecting the entire series).

**Target audience:** any self-employed professional with recurring
clients — this is not a multi-company tool, it's a tool for one person
to manage their own schedule.

**Differentiator from a generic Calendly:** the core of the product is
recurrence with exceptions, not one-off booking.

---

## 2. Scope

### 2.1 In scope (v1)

| # | Feature | Description |
|---|---|---|
| 1 | Professional signup | Account creation and availability configuration |
| 2 | Client registration | Professional registers clients linked to their own account |
| 3 | Recurring session | Defining a recurrence rule (day of week + time) per client |
| 4 | Exception management | Cancelling or rescheduling a specific occurrence without affecting the series |
| 5 | Schedule view | List/calendar of all occurrences (past and future) |
| 6 | Automatic reminder | Email sent X hours before the session |

### 2.2 Out of scope (v1)

- Payments/billing
- Multiple professionals under one account (team/company)
- Google Calendar / Outlook integration
- Native mobile app
- Client confirmation via link (client only receives passive notifications)
- Waitlist / group scheduling (candidate for v2)

---

## 3. Technical stack

| Layer | Technology | Rationale |
|---|---|---|
| Backend | Spring Boot 3.x (Java 21) | Reuses knowledge from the previous project (habit-forge); focus stays on architecture, not the learning curve |
| Frontend | React + TypeScript | Same |
| Data fetching | TanStack Query | Server state caching/sync already validated in the previous project |
| Routing | TanStack Router | Same |
| Validation (frontend) | Zod | Typed form validation |
| Styling | TailwindCSS | UI productivity without the maintenance cost of custom CSS |
| Database | PostgreSQL 16 | Relational, strong support for constraints and transactions — needed for recurrence rules and optimistic locking |
| ORM | Spring Data JPA (Hibernate) | Standard in the Spring ecosystem; supports `@Version` natively for optimistic locking |
| Observability | Spring Boot Actuator + SLF4J/Logback (JSON) | Health checks and structured logging, production-ready |
| Testing | JUnit 5 + Testcontainers | Integration tests against a real Postgres instance, not mocks |
| Containerization | Docker | Consistent local environment and deployment base |
| CI/CD | GitHub Actions | Free, integrated with the repository |
| Deploy | Railway / Render / Fly.io (TBD) | Low cost, suitable for a portfolio project |

See `docs/adr/001-stack-choice.md` for the full rationale.

---

## 4. Domain modeling

### 4.1 Entities

**Professional**
- `id` (UUID, PK)
- `name` (string)
- `email` (string, unique)
- `password_hash` (string)
- `created_at` (timestamp)

**Client**
- `id` (UUID, PK)
- `professional_id` (UUID, FK → Professional)
- `name` (string)
- `email` (string)
- `created_at` (timestamp)

**RecurringSession**
- `id` (UUID, PK)
- `client_id` (UUID, FK → Client)
- `day_of_week` (enum: MONDAY..SUNDAY)
- `start_time` (time)
- `end_time` (time)
- `effective_start_date` (date)
- `effective_end_date` (date, nullable — null = no defined end)
- `version` (int — optimistic locking control, `@Version`)
- `updated_at` (timestamp)

**Occurrence**
- `id` (UUID, PK)
- `recurring_session_id` (UUID, FK → RecurringSession, nullable — null if one-off)
- `date` (date)
- `start_time` (time)
- `end_time` (time)
- `status` (enum: SCHEDULED, CANCELLED, RESCHEDULED, COMPLETED)
- `created_at` (timestamp)

**Notification**
- `id` (UUID, PK)
- `occurrence_id` (UUID, FK → Occurrence)
- `type` (enum: REMINDER)
- `status` (enum: PENDING, SENT, FAILED)
- `sent_at` (timestamp, nullable)

### 4.2 Core business rules

1. **Occurrence materialization:** a scheduled job ensures `occurrence`
   rows always exist for the next 8 weeks from each active
   `recurringSession`.
2. **Cancellation doesn't delete:** a cancelled `occurrence` has its
   `status` changed to `CANCELLED`, and is never removed from the
   database (history and future metrics).
3. **Series edit vs. occurrence edit:** editing a `recurringSession`
   (e.g., changing the time) does not retroactively change already
   materialized occurrences — only future ones, generated after the edit.
4. **Concurrency:** every write to `recurringSession` uses optimistic
   locking (`version`) to prevent the materialization job and a manual
   edit from causing inconsistency (see ADR 003).
5. **Notification idempotency:** the reminder job only processes
   `Notification`s with `status = PENDING`, making the job safe to
   re-run.

---

## 5. API specification (REST)

Convention: plural resources, JWT authentication (Bearer token) on all
routes except `/auth/*`.

### 5.1 Authentication

| Method | Route | Description |
|---|---|---|
| POST | `/auth/register` | Creates a professional account |
| POST | `/auth/login` | Authenticates and returns a JWT |

### 5.2 Clients

| Method | Route | Description |
|---|---|---|
| GET | `/clients` | Lists the authenticated professional's clients |
| POST | `/clients` | Creates a client |
| GET | `/clients/{id}` | Retrieves a client |
| PUT | `/clients/{id}` | Updates a client |
| DELETE | `/clients/{id}` | Removes a client |

### 5.3 Recurring sessions

| Method | Route | Description |
|---|---|---|
| GET | `/clients/{clientId}/recurring-sessions` | Lists recurrence rules for a client |
| POST | `/clients/{clientId}/recurring-sessions` | Creates a recurrence rule |
| PUT | `/recurring-sessions/{id}` | Updates a rule (requires `version` in the body — optimistic locking) |
| DELETE | `/recurring-sessions/{id}` | Ends a recurrence rule |

### 5.4 Occurrences (schedule)

| Method | Route | Description |
|---|---|---|
| GET | `/occurrences?start=&end=` | Lists a professional's occurrences within a date range |
| PATCH | `/occurrences/{id}/cancel` | Cancels a specific occurrence (exception) |
| PATCH | `/occurrences/{id}/reschedule` | Reschedules a specific occurrence to a new date/time |

### 5.5 Standard error format

```json
{
  "error": "RECURRING_SESSION_NOT_FOUND",
  "message": "Recurring session with id {id} not found.",
  "timestamp": "2026-07-27T14:30:00Z"
}
```

A version conflict (optimistic locking) returns HTTP `409 Conflict` with
`error: "VERSION_CONFLICT"`.

---

## 6. Non-functional requirements

| Requirement | Implementation |
|---|---|
| Structured logging | SLF4J + Logback, JSON output, with `professional_id` and `occurrence_id` in context when applicable |
| Standardized error handling | `@ControllerAdvice` + `@ExceptionHandler` returning the format from section 5.5 |
| Health check | `/actuator/health` endpoint via Spring Boot Actuator |
| Idempotency | Notification job only processes `status = PENDING` |
| Testing | Unit tests for materialization/recurrence logic; integration tests with Testcontainers for real persistence |
| Concurrency | Optimistic locking (`@Version`) on `recurringSession` |

---

## 7. Outside this document

Specific technical decisions (the "why" behind each choice above) are
recorded individually in `docs/adr/`:

- `001-stack-choice.md`
- `002-occurrence-materialization.md`
- `003-optimistic-locking-for-concurrency.md`

This document (`PROJECT_SPEC.md`) describes the **what**; the ADRs
describe the **why**.
