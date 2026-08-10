package loopwork.backend.professional;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfessionalServiceTest {

    @Mock
    ProfessionalRepository professionalRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    ProfessionalService professionalService;

    @Test
    void shouldCreateProfessionalWhenEmailDoesntExists() {
        ProfessionalRequest professionalRequest = new ProfessionalRequest(
                "Professional",
                "professional@example.com",
                "password"
        );

        when(professionalRepository.existsByEmail("professional@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashed-password");
        when(professionalRepository.save(any(Professional.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProfessionalResponse professional = professionalService.create(professionalRequest);

        assertEquals("Professional", professional.name());
        assertEquals("professional@example.com", professional.email());
        verify(passwordEncoder).encode("password");
    }

    @Test
    void shouldThrowProfessionalWhenEmailAlreadyExists() {
        ProfessionalRequest professionalRequest = new ProfessionalRequest(
                "Professional",
                "professional@example.com",
                "password"
        );

        when(professionalRepository.existsByEmail("professional@example.com")).thenReturn(true);


        assertThrows(EmailAlreadyInUseException.class, () -> professionalService.create(professionalRequest));

    }
}
