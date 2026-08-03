package loopwork.backend.occurrence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record OccurrenceResponse(
        String id,
        String recurringSessionId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        OccurrenceStatus occurrenceStatus,
        LocalDateTime createdAt
) {

    public static OccurrenceResponse fromEntity(Occurrence occurrence) {
        return new OccurrenceResponse(
                occurrence.getId(),
                occurrence.getRecurringSession().getId(),
                occurrence.getDate(),
                occurrence.getStartTime(),
                occurrence.getEndTime(),
                occurrence.getStatus(),
                occurrence.getCreatedAt()
        );
    }
}