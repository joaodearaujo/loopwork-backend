package loopwork.backend.recurringSession;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public record RecurringSessionResponse(
        String id,
        String clientId,
        String clientName,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate effectiveStartDate,
        LocalDate effectiveEndDate
) {
    public static RecurringSessionResponse fromEntity(RecurringSession session) {
        return new RecurringSessionResponse(
                session.getId(),
                session.getClient().getId(),
                session.getClient().getName(),
                session.getDayOfWeek(),
                session.getStartTime(),
                session.getEndTime(),
                session.getEffectiveStartDate(),
                session.getEffectiveEndDate()
        );
    }
}