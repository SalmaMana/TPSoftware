
package ma.um6p.compusconnect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/*
 * Service responsible for sending and retrieving messages.
 *
 * It ensures that students can only interact with
 * conversations in which they participate.
 */
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final StudentRepository studentRepository;
    // Constructor injection for the required repositories.
    public MessageService(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            StudentRepository studentRepository) {

        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.studentRepository = studentRepository;
    }

    /*
     * Sends a message inside an existing conversation.
     */
    @Transactional
    public Message sendMessage(
            Long conversationId,
            Long senderId,
            String content) {
        // Reject empty or blank messages.
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "Message content cannot be empty."
            );
        }
        // Retrieve the conversation.
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation not found."
                ));
        // Verify that the sender exists.
        Student sender = studentRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sender not found."
                ));
        // Check whether the sender belongs to this conversation.
        Long participant1Id =
                conversation.getParticipant1().getUserId();

        Long participant2Id =
                conversation.getParticipant2().getUserId();

        boolean isParticipant =
                senderId.equals(participant1Id)
                        || senderId.equals(participant2Id);
        if (!isParticipant) {
            throw new UnauthorizedActionException(
                    "You are not allowed to send messages in this conversation."
            );
        }
        // Create the message and associate it with its sender.
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content.trim());
        return messageRepository.save(message);
    }

    /*
     * Retrieves all messages from a conversation.
     */
    @Transactional(readOnly = true)
    public List<Message> getConversationMessages(
            Long conversationId,
            Long studentId) {
        // Retrieve the conversation.
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation not found."
                ));
        // Verify that the requesting student participates.
        Long participant1Id =
                conversation.getParticipant1().getUserId();
        Long participant2Id =
                conversation.getParticipant2().getUserId();
        boolean isParticipant =
                studentId.equals(participant1Id)
                        || studentId.equals(participant2Id);

        if (!isParticipant) {
            throw new UnauthorizedActionException(
                    "You cannot access this conversation."
            );
        }
        // Return messages in chronological order.
        return messageRepository
                .findByConversation_ConversationIdOrderBySentAtAsc(
                        conversationId
                );
    }
}

