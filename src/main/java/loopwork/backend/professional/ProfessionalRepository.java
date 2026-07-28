package loopwork.backend.professional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalRepository extends JpaRepository<Professional, String> {
    boolean existsByEmail(String email);
}
