package loopwork.backend.client;

public class ClientEmailAlreadyInUseException extends RuntimeException {
    public ClientEmailAlreadyInUseException(String email, String professionalId) {
        super("Client with email " + email + " already exists for professional " + professionalId);
    }
}
