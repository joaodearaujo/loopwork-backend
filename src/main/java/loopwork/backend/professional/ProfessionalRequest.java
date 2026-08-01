package loopwork.backend.professional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfessionalRequest(
        @NotBlank(message = "Name must not be null or blank.")
        @Size(min = 3, max = 55, message = "Name must be between 3 and 55 characters.")
        String name,

        @NotBlank(message = "Email must not be null or blank.")
        @Email(message = "Email must be a well-formed email address.")
        String email,

        @NotBlank(message = "Password must not be null or blank.")
        String password
) {
}