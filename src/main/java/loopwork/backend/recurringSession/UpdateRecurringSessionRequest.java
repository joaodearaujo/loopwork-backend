package loopwork.backend.recurringSession;

import jakarta.validation.constraints.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateRecurringSessionRequest(
        @NotBlank(message = "Client identifier must not be null or blank.")
        @Size(min = 1, max = 36, message = "Client identifier must be between 1 and 36 characters.")
        String clientId,

        DayOfWeek dayOfWeek,

        LocalTime startTime,

        LocalTime endTime,

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