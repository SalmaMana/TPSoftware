package ma.um6p.compusconnect;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    // Student who proposes the session.
    @ManyToOne
    @JoinColumn(name = "proposer_id", nullable = false)
    private Student proposer;

    // Student who receives the proposal.
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Student receiver;

    // Proposed date and time.
    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    // Topic, location and other meeting details.
    @Column(nullable = false, length = 2000)
    private String details;

    // Current status of the session.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.PENDING;

    public Long getSessionId() {
        return sessionId;
    }

    public Student getProposer() {
        return proposer;
    }

    public void setProposer(Student proposer) {
        this.proposer = proposer;
    }

    public Student getReceiver() {
        return receiver;
    }

    public void setReceiver(Student receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}