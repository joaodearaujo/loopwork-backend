package loopwork.backend.occurrence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OccurrenceRepository extends JpaRepository<Occurrence, String> {
    List<Occurrence> findByRecurringSessionId(String recurringSessionId);
    boolean existsByRecurringSessionIdAndDate(String recurringSessionId, LocalDate currentDate);

    @Query("""
        SELECT o FROM Occurrence o
        WHERE o.recurringSession.client.professional.id = :professionalId
        AND o.date BETWEEN :start AND :end
    """)
    List<Occurrence> findByProfessionalIdAndDateRange(
            @Param("professionalId") String professionalId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
