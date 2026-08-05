package loopwork.backend.client;

import loopwork.backend.exception.ResourceNotFoundException;

public class ClientNotFoundException extends ResourceNotFoundException {
    public ClientNotFoundException(String clientId) {
        super("Professional not found with id: " + clientId);
    }
}