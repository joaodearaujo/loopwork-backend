# ADR 002: Eager materialization of occurrences

## Status
Accepted

## Context
Recurring sessions (e.g., "every Tuesday at 6 PM") need to be represented
in a way that supports one-off exceptions (cancelling/rescheduling a
specific date without affecting the whole series). Two approaches were
possible for modeling this.

## Options considered

**A — On-demand generation:** store only the recurrence rule and
dynamically calculate occurrences whenever the schedule is queried.
Exceptions live in a separate "overrides" table applied on top of the
calculation.

**B — Eager materialization:** physically generate each occurrence as a
row in the database (e.g., the next 8 weeks) via a periodic job.
Exceptions are a direct `UPDATE` on an existing row.

## Decision
Option B — eager materialization, via a job that ensures future
occurrences exist for the next 8 weeks from each active
`recurringSession`.

## Consequences
- **Gain:** a simpler model to reason about, test, and debug — each
  occurrence is a real row, with no dynamic date-calculation logic.
  Cancellations and reschedules are trivial operations (a status `UPDATE`).
- **Gain:** consistent with history (past occurrences continue to exist
  as records, useful for future metrics).
- **Loss:** requires a job running periodically to "extend" the schedule;
  if the job fails for an extended period, the schedule may run out of
  sufficient future occurrences.
- **Resulting concern:** materialization running concurrently with edits
  to the recurrence rule introduces a risk of inconsistency — addressed
  in ADR 003.
