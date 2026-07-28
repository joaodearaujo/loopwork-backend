package loopwork.backend.professional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfessionalRequest(
        @NotBlank
        @Size(min = 3, max = 55)
        String name,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
