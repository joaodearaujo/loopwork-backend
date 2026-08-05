package loopwork.backend.recurringSession;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public record RecurringSessionRequest(

        @NotNull(message = "Day of the week is required.")
        DayOfWeek dayOfWeek,

        @NotNull(message = "Start time is required.")
        LocalTime startTime,

        @NotNull(message = "End time is required.")
        LocalTime endTime,

        @NotNull(message = "Effective start date is required.")
        @FutureOrPresent(message = "Effective start date must be in the present or future.")
        LocalDate effectiveStartDate,

        LocalDate effectiveEndDate
) {

    @AssertTrue(message = "End time must be after the start time.")
    private boolean isTimeValid() {
        if (startTime == null || endTime == null) return true;
        return endTime.isAfter(startTime);
    }

    @AssertTrue(message = "Effective end date must be equal to or after the effective start date.")
    private boolean isDateValid() {
        if (effectiveStartDate == null || effectiveEndDate == null) return true;
        return !effectiveEndDate.isBefore(effectiveStartDate);
    }
}