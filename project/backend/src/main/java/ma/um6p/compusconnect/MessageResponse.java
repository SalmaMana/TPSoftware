
package ma.um6p.compusconnect;
import java.time.LocalDateTime;
public class MessageResponse {
    private Long messageId;
    private Long conversationId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
    // Build the response using the saved Message entity.
    public MessageResponse(Message message) {
        this.messageId = message.getMessageId();
        this.conversationId = message.getConversation().getConversationId();
        this.senderId = message.getSender().getUserId();
        this.senderName = message.getSender().getFullName();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
    }
    public Long getMessageId() {
        return messageId;
    }
    public Long getConversationId() {
        return conversationId;
    }
    public Long getSenderId() {
        return senderId;
    }
    public String getSenderName() {
        return senderName;
    }
    public String getContent() {
        return content;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
