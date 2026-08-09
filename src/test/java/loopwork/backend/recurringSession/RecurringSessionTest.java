package loopwork.backend.recurringSession;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

class RecurringSessionTest {

    @Test
    void shouldUpdateScheduleWhenHasValidTime() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        session.updateSchedule(LocalTime.of(19, 0), LocalTime.of(20, 0));

        Assertions.assertEquals(LocalTime.of(19, 0), session.getStartTime());
        Assertions.assertEquals(LocalTime.of(20, 0), session.getEndTime());
    }

    @Test
    void shouldThrowAnExceptionWhenEndTimeIsBeforeStartTime() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        Assertions.assertThrows(InvalidRecurringSessionScheduleException.class, () ->
                session.updateSchedule(LocalTime.of(19, 0), LocalTime.of(18, 0))
        );
    }

    @Test
    void shouldKeepEndTimeWhenOnlyStartTimeIsUpdated() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        session.updateSchedule(LocalTime.of(17, 0), null);

        Assertions.assertEquals(LocalTime.of(17, 0), session.getStartTime());
        Assertions.assertEquals(LocalTime.of(19, 0), session.getEndTime());
    }

    @Test
    void shouldUpdateEffectivePeriodWhenDatesAreValid() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        LocalDate newStart = LocalDate.now().plusDays(3);
        LocalDate newEnd = LocalDate.now().plusWeeks(4);

        session.updateEffectivePeriod(newStart, newEnd);

        Assertions.assertEquals(newStart, session.getEffectiveStartDate());
        Assertions.assertEquals(newEnd, session.getEffectiveEndDate());
    }

    @Test
    void shouldThrowWhenEffectiveEndDateIsBeforeStartDate() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        LocalDate newStart = LocalDate.now().plusWeeks(2);
        LocalDate newEnd = LocalDate.now().plusDays(1);

        Assertions.assertThrows(InvalidRecurringSessionScheduleException.class, () ->
                session.updateEffectivePeriod(newStart, newEnd)
        );
    }

    @Test
    void shouldAllowNullEffectiveEndDateAsNoEndDefined() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        LocalDate newStart = LocalDate.now().plusDays(3);

        session.updateEffectivePeriod(newStart, null);

        Assertions.assertEquals(newStart, session.getEffectiveStartDate());
        Assertions.assertNull(session.getEffectiveEndDate());
    }

    @Test
    void shouldUpdateDayOfWeek() {
        RecurringSession session = new RecurringSession(
                null, DayOfWeek.TUESDAY,
                LocalTime.of(18, 0), LocalTime.of(19, 0),
                LocalDate.now(), null
        );

        session.updateDayOfWeek(DayOfWeek.WEDNESDAY);

        Assertions.assertEquals(DayOfWeek.WEDNESDAY, session.getDayOfWeek());
    }
}