package loopwork.backend.client;

public record ClientResponse(
        String id,
        String name,
        String email
) {
    public static ClientResponse fromEntity(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getEmail()
        );
    }
}
