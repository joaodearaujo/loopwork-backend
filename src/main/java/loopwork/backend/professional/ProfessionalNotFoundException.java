package loopwork.backend.professional;

public class ProfessionalNotFoundException extends  RuntimeException {
    public ProfessionalNotFoundException(String professionalId) {
        super("Professional not found with id: " + professionalId);
    }
}
