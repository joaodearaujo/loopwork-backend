package loopwork.backend.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank
        @Size(min = 3, max = 55)
        String name,

        @Email
        String email
) {
}
