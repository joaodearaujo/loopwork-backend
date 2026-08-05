package loopwork.backend.professional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, String> {
    boolean existsByEmail(String email);
    Optional<Professional> findByEmail(String email);
}
