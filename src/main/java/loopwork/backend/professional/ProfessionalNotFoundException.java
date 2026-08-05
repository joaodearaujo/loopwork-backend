package loopwork.backend.professional;
import loopwork.backend.exception.ResourceNotFoundException;

public class ProfessionalNotFoundException extends ResourceNotFoundException {
    public ProfessionalNotFoundException(String professionalId) {
        super("Professional not found with id: " + professionalId);
    }
}
