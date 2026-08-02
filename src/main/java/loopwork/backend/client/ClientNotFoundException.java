package loopwork.backend.client;

public class ClientNotFoundException extends  RuntimeException {
    public ClientNotFoundException(String clientId) {
        super("Professional not found with id: " + clientId);
    }
}