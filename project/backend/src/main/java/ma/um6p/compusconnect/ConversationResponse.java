
package ma.um6p.compusconnect;
import java.time.LocalDateTime;
public class ConversationResponse {
    private Long conversationId;
    private Long participant1Id;
    private String participant1Name;
    private Long participant2Id;
    private String participant2Name;
    private LocalDateTime creationDate;
    // Build the response using the Conversation entity.
    public ConversationResponse(Conversation conversation) {
        this.conversationId = conversation.getConversationId();
        this.participant1Id = conversation.getParticipant1().getUserId();
        this.participant1Name = conversation.getParticipant1().getFullName();
        this.participant2Id = conversation.getParticipant2().getUserId();
        this.participant2Name = conversation.getParticipant2().getFullName();
        this.creationDate = conversation.getCreationDate();
    }

    public Long getConversationId() {
        return conversationId;
    }

    public Long getParticipant1Id() {
        return participant1Id;
    }

    public String getParticipant1Name() {
        return participant1Name;
    }

    public Long getParticipant2Id() {
        return participant2Id;
    }

    public String getParticipant2Name() {
        return participant2Name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}

