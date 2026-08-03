package loopwork.backend.notification;

import jakarta.persistence.*;
import loopwork.backend.occurrence.Occurrence;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "occurrence_id", nullable = false)
    private Occurrence occurrence;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Notification(Occurrence occurrence, NotificationType notificationType, NotificationStatus notificationStatus) {
        this.occurrence = occurrence;
        this.notificationType = notificationType;
        this.notificationStatus = notificationStatus;
    }

    public Notification() {}

    public String getId() {
        return id;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public NotificationType getType() {
        return notificationType;
    }

    public void setType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationStatus getStatus() {
        return notificationStatus;
    }

    public void setStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
