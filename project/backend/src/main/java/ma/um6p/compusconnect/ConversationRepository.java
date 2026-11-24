package ma.um6p.compusconnect;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/*
 * Repository responsible for database operations
 * related to conversations.
 *
 * JpaRepository provides built-in methods such as:
 * save(), findById(), findAll() and deleteById().
 */
public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {
    /*
     * Retrieves all conversations where the student
     * participates as either participant1 or participant2.
     */
    List<Conversation> findByParticipant1_UserIdOrParticipant2_UserId(
            Long participant1Id,
            Long participant2Id
    );
}