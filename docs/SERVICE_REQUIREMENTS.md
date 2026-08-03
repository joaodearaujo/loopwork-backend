# Loopwork — Service Layer Requirements (Pending Implementation)

This document describes the expected behavior for `RecurringSessionService`,
`OccurrenceService`, and `NotificationService`. Written as a specification
to implement against — no code included by design.

---

## RecurringSessionService

### `create(RecurringSessionRequest request, String clientId) -> RecurringSessionResponse`
- Look up the `Client` by `clientId`; throw `InvalidOccurrenceStatusException` if not found
- Build a new `RecurringSession` from the request + the found client
- Save it, convert to `RecurringSessionResponse`, return it

### `getRecurringSessionsOfClient(String clientId) -> List<RecurringSessionResponse>`
- Validate the client exists (fail loudly instead of silently returning an
  empty list)
- Fetch via `recurringSessionRepository.findByClientId(clientId)`
- Map each result to `RecurringSessionResponse`

### `findByIdOrThrow(String id) -> RecurringSession`
- Returns the entity, not a DTO (other services need the real object)
- Throws `RecurringSessionNotFoundException(id)` if absent

### `update(String id, UpdateRecurringSessionRequest request) -> RecurringSessionResponse`
- Fetch the entity via `findByIdOrThrow`
- Apply changes from the request
- On save, a `version` mismatch triggers `OptimisticLockException` — decide
  whether to let it propagate or catch and rethrow as a custom exception.
  Expected outcome either way: a 409-worthy signal, never a silent overwrite

---

## OccurrenceService

### `materializeUpcomingOccurrences() -> void` (or a count, your choice)
- No request/response DTOs — triggered by `@Scheduled`, not an HTTP call
- For each **active** `RecurringSession`, walk forward week by week up to
  +8 weeks from today
- Skip dates before `effectiveStartDate` or after `effectiveEndDate` (if set)
- For each valid date: if no `Occurrence` exists yet for that
  `(recurringSession, date)` pair, create one with `status = SCHEDULED`
- Needs a repository method to fetch **all active** recurring sessions
  (not `findByIdOrThrow`, which only fetches one)

### `getOccurrencesOfProfessional(String professionalId, LocalDate start, LocalDate end) -> List<OccurrenceResponse>`
- Matches `GET /occurrences?start=&end=` from the spec
- Open design question: `Occurrence` has no direct `professionalId` — needs
  a way to traverse `Occurrence -> RecurringSession -> Client -> Professional`,
  likely via a repository query method or a join

### `cancel(String occurrenceId) -> OccurrenceResponse`
- Fetch the occurrence, throw `OccurrenceNotFoundException` if absent
- Business rule: only `SCHEDULED` occurrences can be cancelled — decide what
  happens otherwise (reject with a clear exception)
- Consider placing this rule inside `Occurrence.cancel()` itself, not in
  the Service (entity protects its own invariants)

### `reschedule(String occurrenceId, RescheduleRequest request) -> OccurrenceResponse`
- Fetch the occurrence, validate it can be rescheduled (same status
  constraint as `cancel`)
- Update `date`/`startTime`/`endTime`, set `status = RESCHEDULED`

---

## NotificationService

### `createPendingReminder(Occurrence occurrence) -> Notification`
- Not exposed via HTTP — called internally, likely right after an
  `Occurrence` is materialized
- Creates a `Notification` with `type = REMINDER`, `status = PENDING`,
  `sentAt = null`

### `sendPendingReminders() -> void`
- Separate `@Scheduled` job from materialization
- Fetches all `Notification`s with `status = PENDING` where the related
  occurrence's date/time falls within the reminder window (threshold TBD)
- For each: attempt to send the email, then update `status` to `SENT`
  (and set `sentAt = now()`) or `FAILED`
- Must be safe to run repeatedly without double-sending — enforced by only
  ever querying `status = PENDING`

---

## Open decisions before implementation

- [ ] How to query `Occurrence`s by `professionalId` (traversal path)
- [ ] Reminder window threshold (how many hours before the session)
- [ ] Whether `OptimisticLockException` is caught in the Service or
      propagated to a future `@ControllerAdvice`
- [ ] Exact editable fields for `RecurringSession.update`
