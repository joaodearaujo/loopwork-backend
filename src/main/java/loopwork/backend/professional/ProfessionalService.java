package loopwork.backend.professional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfessionalService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessionalService.class);
    private final ProfessionalRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ProfessionalService(ProfessionalRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Professional findByIdOrThrow(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));
    }

    public ProfessionalResponse create(ProfessionalRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyInUseException(request.email());
        }

        String passwordHash = passwordEncoder.encode(request.password());

        Professional professional = new Professional(
                request.name(),
                request.email(),
                passwordHash
        );

        Professional savedProfessional = repository.save(professional);

        logger.info("Professional created successfully");

        return ProfessionalResponse.fromEntity(savedProfessional);

    }
}
