package loopwork.backend.professional;

public record ProfessionalResponse(
        String id,
        String email,
        String name
) {
    public static ProfessionalResponse fromEntity(Professional professional) {
        return new ProfessionalResponse(
                professional.getId(),
                professional.getEmail(),
                professional.getName()
        );
    }
}