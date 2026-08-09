package loopwork.backend.occurrence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

class OccurrenceTest {

    @Test
    void shouldCancelWhenScheduled() {
        Occurrence occurrence = new Occurrence(
                null,
                LocalDate.now(),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                OccurrenceStatus.SCHEDULED
        );

        occurrence.cancel();

        Assertions.assertEquals(OccurrenceStatus.CANCELLED, occurrence.getStatus());
    }

    @Test
    void shouldThrowWhenCancellingNonScheduledOccurrence() {
        Occurrence occurrence = new Occurrence(
                null,
                LocalDate.now(),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                OccurrenceStatus.CANCELLED
        );

        Assertions.assertThrows(InvalidOccurrenceStatusException.class, () -> occurrence.cancel());
    }

    @Test
    void shouldRescheduleWhenScheduled() {
        Occurrence occurrence = new Occurrence(
                null,
                LocalDate.now(),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                OccurrenceStatus.SCHEDULED
        );

        LocalDate newDate = LocalDate.now().plusWeeks(1);
        LocalTime newStart = LocalTime.of(20, 0);
        LocalTime newEnd = LocalTime.of(21, 0);

        occurrence.reschedule(newDate, newStart, newEnd);

        Assertions.assertEquals(OccurrenceStatus.RESCHEDULED, occurrence.getStatus());
    }

    @Test
    void shouldThrowWhenReschedulingNonScheduledOccurrence() {
        Occurrence occurrence = new Occurrence(
                null,
                LocalDate.now(),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                OccurrenceStatus.CANCELLED
        );

        Assertions.assertThrows(InvalidOccurrenceStatusException.class, () ->
                occurrence.reschedule(LocalDate.now(), LocalTime.of(20, 0), LocalTime.of(21, 0))
        );
    }
}