package ma.um6p.compusconnect;
import jakarta.persistence.*;
import java.time.LocalDateTime;
/*
 * Entity representing a private conversation between two students.
 *
 * Each conversation has:
 * - Two participants.
 * - A creation date.
 *
 * The conversation will later be used to organize messages
 * and enforce access control between its participants.
 */
@Entity
public class Conversation {
    // Unique identifier of the conversation.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;
    /*
     * First participant of the conversation.
     * References a Student stored in the User table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant1_id", nullable = false)
    private Student participant1;
    /*
     * Second participant of the conversation.
     * References another Student stored in the User table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant2_id", nullable = false)
    private Student participant2;
    // Date and time when the conversation was created.
    private LocalDateTime creationDate;
    /*
     * Automatically assigns the creation date
     * before the conversation is inserted into the database.
     */
    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }
    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getConversationId() {
        return conversationId;
    }

    public Student getParticipant1() {
        return participant1;
    }

    public void setParticipant1(Student participant1) {
        this.participant1 = participant1;
    }

    public Student getParticipant2() {
        return participant2;
    }

    public void setParticipant2(Student participant2) {
        this.participant2 = participant2;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
