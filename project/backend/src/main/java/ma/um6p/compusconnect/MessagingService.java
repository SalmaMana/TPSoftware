
package ma.um6p.compusconnect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/*
 * Service responsible for managing conversations.
 *
 * It contains the business logic between the controller
 * and the database repository.
 */
@Service
public class MessagingService {
    private final ConversationRepository conversationRepository;
    private final StudentRepository studentRepository;
    // Constructor injection: Spring provides both repositories.
    public MessagingService(
            ConversationRepository conversationRepository,
            StudentRepository studentRepository) {

        this.conversationRepository = conversationRepository;
        this.studentRepository = studentRepository;
    }

    /*
     * Creates a conversation between two existing students.
     */
    @Transactional
    public Conversation createConversation(Long student1Id, Long student2Id) {
        // A student cannot start a conversation with themselves.
        if (student1Id.equals(student2Id)) {
            throw new IllegalArgumentException(
                    "A student cannot create a conversation with themselves."
            );
        }
        // Retrieve both students from the database.
        Student student1 = studentRepository.findById(student1Id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "First student not found."
                ));

        Student student2 = studentRepository.findById(student2Id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Second student not found."
                ));

        // Check whether a conversation already exists between them.
        List<Conversation> existingConversations =
                conversationRepository
                        .findByParticipant1_UserIdOrParticipant2_UserId(
                                student1Id, student2Id
                        );

        // Avoid creating duplicate conversations.
        for (Conversation conversation : existingConversations) {

            Long firstId = conversation.getParticipant1().getUserId();
            Long secondId = conversation.getParticipant2().getUserId();

            if ((firstId.equals(student1Id) && secondId.equals(student2Id))
                    || (firstId.equals(student2Id) && secondId.equals(student1Id))) {

                return conversation;
            }
        }

        // Create and save a new conversation.
        Conversation conversation = new Conversation();
        conversation.setParticipant1(student1);
        conversation.setParticipant2(student2);

        return conversationRepository.save(conversation);
    }

    /*
     * Retrieves all conversations involving a particular student.
     */
    @Transactional(readOnly = true)
    public List<Conversation> getStudentConversations(Long studentId) {
        // Verify that the student exists.
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found.");
        }
        return conversationRepository
                .findByParticipant1_UserIdOrParticipant2_UserId(
                        studentId, studentId
                );
    }
}

