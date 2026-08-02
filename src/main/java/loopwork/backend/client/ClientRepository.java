package loopwork.backend.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {
    List<Client> findByProfessionalId(String professionalId);
    boolean existsByProfessionalIdAndEmail(String professionalId, String email);

}
