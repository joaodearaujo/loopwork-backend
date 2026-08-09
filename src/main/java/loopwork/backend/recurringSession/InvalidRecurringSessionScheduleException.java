package loopwork.backend.recurringSession;

public class InvalidRecurringSessionScheduleException extends RuntimeException {
    public InvalidRecurringSessionScheduleException(String message) {
        super(message);
    }
}