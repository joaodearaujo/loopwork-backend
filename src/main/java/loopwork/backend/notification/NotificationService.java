package loopwork.backend.notification;

import loopwork.backend.occurrence.Occurrence;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification createPendingReminder(Occurrence occurrence) {
        return new Notification(
                occurrence,
                NotificationType.REMINDER,
                NotificationStatus.PENDING
        );
    }

    public void sendPendingReminders() {
        // TODO: implement once an email provider is integrated (e.g. SendGrid, Resend)
        // Fetch PENDING notifications, attempt to send, mark as SENT or FAILED
    }
}
