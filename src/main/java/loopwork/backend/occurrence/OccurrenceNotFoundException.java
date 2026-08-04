package loopwork.backend.occurrence;

import loopwork.backend.exception.ResourceNotFoundException;

public class OccurrenceNotFoundException extends ResourceNotFoundException {
    public OccurrenceNotFoundException(String id) {
        super("Occurrence not found with id: " + id);
    }
}
