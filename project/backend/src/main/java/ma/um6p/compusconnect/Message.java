
package ma.um6p.compusconnect;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 * Entity representing an individual message
 * sent by a student inside a conversation.
 */
@Entity
public class Message {
    // Unique identifier of the message.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    /*
     * Conversation to which this message belongs.
     * A conversation can contain multiple messages.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;
    /*
     * Student who sent this message.
     * A student can send multiple messages.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Student sender;
    // Actual text content of the message.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    // Date and time when the message was sent.
    private LocalDateTime sentAt;
    /*
     * Automatically assigns the sending date
     * before the message is inserted into the database.
     */
    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
    // =========================
    // GETTERS AND SETTERS
    // =========================
    public Long getMessageId() {
        return messageId;
    }
    public Conversation getConversation() {
        return conversation;
    }
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    public Student getSender() {
        return sender;
    }
    public void setSender(Student sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
}

