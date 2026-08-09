# ADR 003: Optimistic locking for concurrency on RecurringSession

## Status
Accepted

## Context
With eager materialization (ADR 002), there's a race scenario: the
materialization job can read a `recurringSession` at the exact moment
the professional is editing that same rule (e.g., changing the time).
If the job uses the stale version, it generates future occurrences with
incorrect data.

Unlike a system such as Calendly, there is no concurrency *between
users* competing for the same time slot — each professional only edits
their own schedule. The real risk is between the asynchronous job and
the manual edit.

## Options considered

**a) Always-fresh reads in the job:** the materialization job always
reads the latest state from the database at execution time, with no
caching. Resolves most of the problem, but doesn't prevent an exact race
between simultaneous read and write.

**b) Optimistic locking (`@Version`):** add a `version` column to
`recurringSession`. Every update checks whether the version still
matches the expected one (`UPDATE ... WHERE id = ? AND version = ?`);
if not, the operation fails and is treated as a conflict.

**c) Pessimistic locking (`SELECT FOR UPDATE`):** lock the row in the
database for the entire read+write operation.

## Decision
Option (b) — optimistic locking via JPA/Hibernate's `@Version`.

## Consequences
- **Gain:** real protection against the job-vs-manual-edit race, without
  the performance cost of pessimistic locks — appropriate for the
  expected concurrency volume (one professional editing their own
  schedule, not hundreds of simultaneous users).
- **Gain:** a widely used pattern in the Spring/JPA ecosystem,
  transferable to other projects.
- **Loss:** requires explicit conflict handling (HTTP 409) in the
  frontend/API when the version is stale — not transparent to API
  consumers.
- **Discarded (c):** pessimistic locking would introduce unnecessary
  performance cost for the real concurrency level of this system.
