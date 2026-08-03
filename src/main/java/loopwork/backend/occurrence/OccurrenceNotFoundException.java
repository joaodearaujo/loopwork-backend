package loopwork.backend.occurrence;

public class OccurrenceNotFoundException extends RuntimeException {
    public OccurrenceNotFoundException(String id) {
        super("Occurrence not found with id: " + id);
    }
}
