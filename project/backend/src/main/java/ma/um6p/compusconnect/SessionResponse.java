package ma.um6p.compusconnect;

import java.time.LocalDateTime;

public class SessionResponse {

    private final Long sessionId;
    private final Long proposerId;
    private final String proposerName;
    private final Long receiverId;
    private final String receiverName;
    private final LocalDateTime scheduledAt;
    private final String details;
    private final SessionStatus status;

    public SessionResponse(Session session) {
        this.sessionId = session.getSessionId();

        this.proposerId = session.getProposer().getUserId();
        this.proposerName = session.getProposer().getFullName();

        this.receiverId = session.getReceiver().getUserId();
        this.receiverName = session.getReceiver().getFullName();

        this.scheduledAt = session.getScheduledAt();
        this.details = session.getDetails();
        this.status = session.getStatus();
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getProposerId() {
        return proposerId;
    }

    public String getProposerName() {
        return proposerName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public String getDetails() {
        return details;
    }

    public SessionStatus getStatus() {
        return status;
    }
}