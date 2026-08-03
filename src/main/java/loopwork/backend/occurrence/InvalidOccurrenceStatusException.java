package loopwork.backend.occurrence;

public class InvalidOccurrenceStatusException extends  RuntimeException {
    public InvalidOccurrenceStatusException() {
        super("Only scheduled occurrences can be cancelled");
    }
}