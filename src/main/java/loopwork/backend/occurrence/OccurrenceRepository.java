package loopwork.backend.occurrence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OccurrenceRepository extends JpaRepository<Occurrence, String> {
    List<Occurrence> findByRecurringSessionId(String recurringSessionId);
}
