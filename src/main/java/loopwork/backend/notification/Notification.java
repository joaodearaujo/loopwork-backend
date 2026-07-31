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
    private Type type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Notification(Occurrence occurrence, Type type, NotificationStatus notificationStatus) {
        this.occurrence = occurrence;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
