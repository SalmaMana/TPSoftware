
package ma.um6p.compusconnect;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*
 * Repository responsible for database operations
 * related to messages.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    /*
     * Retrieves all messages belonging to a conversation.
     * Messages are ordered from oldest to newest.
     */
    List<Message> findByConversation_ConversationIdOrderBySentAtAsc(
            Long conversationId
    );
}

