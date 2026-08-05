package loopwork.backend.recurringSession;

import loopwork.backend.exception.ResourceNotFoundException;

public class RecurringSessionNotFoundException extends ResourceNotFoundException {
    public  RecurringSessionNotFoundException(String recurringSessionId) {
        super("RecurringSession not found with id: " + recurringSessionId);
    }
}
