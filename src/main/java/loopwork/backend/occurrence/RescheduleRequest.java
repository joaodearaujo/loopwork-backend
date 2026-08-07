package loopwork.backend.occurrence;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleRequest(
        @NotNull(message = "Date is required.")
        LocalDate newDate,

        @NotNull(message = "Start time is required.")
        LocalTime newStartTime,

        @NotNull(message = "End time is required.")
        LocalTime newEndTime
) { }