package loopwork.backend.authentication;

import loopwork.backend.professional.Professional;
import loopwork.backend.professional.ProfessionalRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticationService(ProfessionalRepository professionalRepository,
                                 PasswordEncoder passwordEncoder,
                                 TokenService tokenService) {
        this.professionalRepository = professionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public String login(LoginRequest request) {
        Professional professional = professionalRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), professional.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return tokenService.generateToken(professional);
    }
}