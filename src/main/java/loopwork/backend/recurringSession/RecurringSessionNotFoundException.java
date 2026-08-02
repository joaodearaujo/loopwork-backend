package loopwork.backend.recurringSession;

public class RecurringSessionNotFoundException extends RuntimeException {
    public  RecurringSessionNotFoundException(String recurringSessionId) {
        super("RecurringSession not found with id: " + recurringSessionId);
    }
}
