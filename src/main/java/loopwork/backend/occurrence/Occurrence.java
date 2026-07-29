package loopwork.backend.occurrence;

import jakarta.persistence.*;
import loopwork.backend.recurringSession.RecurringSession;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "occurrence")
public class Occurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecurringSession recurringSession;

    @Column
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private OccurrenceStatus occurrenceStatus;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Occurrence(RecurringSession recurringSession, LocalDate date, LocalTime startTime, LocalTime endTime, OccurrenceStatus occurrenceStatus) {
        this.recurringSession = recurringSession;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.occurrenceStatus = occurrenceStatus;
        this.createdAt = LocalDateTime.now();
    }

    public Occurrence() {}

    public String getId() {
        return id;
    }

    public RecurringSession getRecurringSession() {
        return recurringSession;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public OccurrenceStatus getStatus() {
        return occurrenceStatus;
    }

    public void setStatus(OccurrenceStatus occurrenceStatus) {
        this.occurrenceStatus = occurrenceStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
