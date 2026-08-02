package loopwork.backend.recurringSession;

import jakarta.persistence.*;
import loopwork.backend.client.Client;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
@Table(name = "recurring_session")
public class RecurringSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "effective_start_date")
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    @Version
    private Integer version;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    public RecurringSession(
            Client client,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            LocalDate effectiveStartDate,
            LocalDate effectiveEndDate
    ) {
        this.client = client;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
    }

    public void updateDayOfWeek(DayOfWeek newDayOfWeek) {
        if (newDayOfWeek != null ) {
            setDayOfWeek(newDayOfWeek);
        }
    }

    public void updateSchedule(LocalTime newStartTime, LocalTime newEndtime) {
        LocalTime finalStartTime = newStartTime != null ? newStartTime : this.startTime;
        LocalTime finalEndTime = newEndtime != null ? newEndtime : this.endTime;

        if (finalEndTime != null && newEndtime.isBefore(finalEndTime)) {
            throw  new RuntimeException("End time must be after the start time.");
        }

        this.startTime = finalStartTime;
        this.endTime = finalStartTime;
    }

    public void updateEffectivePeriod(LocalDate newEffectiveStartDate, LocalDate newEffectiveEndDate) {
        LocalDate finalEffectiveStartDate = newEffectiveStartDate != null ? newEffectiveStartDate : this.effectiveStartDate;
        LocalDate finalEffectiveEndDate = newEffectiveEndDate != null ? newEffectiveEndDate : this.effectiveEndDate;

        if (newEffectiveEndDate != null && newEffectiveEndDate.isBefore(newEffectiveStartDate)) {
            throw  new RuntimeException("End time must be after the start time.");
        }

        this.effectiveStartDate = finalEffectiveStartDate;
        this.effectiveEndDate = finalEffectiveEndDate;
    }

    public RecurringSession() {}

    public String getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public LocalDate getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(LocalDate effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public LocalDate getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(LocalDate effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public Integer getVersion() {
        return version;
    }
}
