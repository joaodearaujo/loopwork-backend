package loopwork.backend.occurrence;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record OccurrenceRequest(
        @NotBlank(message = "Recurring session identifier must not be null or blank.")
        @Size(min = 1, max = 36, message = "Recurring session identifier must be between 1 and 36 characters.")
        String recurringSessionId,

        @NotNull(message = "Date is required.")
        LocalDate date,

        @NotNull(message = "Start time is required.")
        LocalTime startTime,

        @NotNull(message = "End time is required.")
        LocalTime endTime,

        @NotNull(message = "Occurrence status is required.")
        OccurrenceStatus occurrenceStatus
) { }