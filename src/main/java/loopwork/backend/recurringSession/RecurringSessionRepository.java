package loopwork.backend.recurringSession;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecurringSessionRepository extends JpaRepository<RecurringSession, String> {
    List<RecurringSession> findByClientId(String clientId);
}
