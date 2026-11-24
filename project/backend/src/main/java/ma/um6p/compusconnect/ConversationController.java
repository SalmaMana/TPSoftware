
package ma.um6p.compusconnect;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final MessagingService messagingService;
    private final StudentRepository studentRepository;
    // Constructor injection
    public ConversationController(
            MessagingService messagingService,
            StudentRepository studentRepository) {
        this.messagingService = messagingService;
        this.studentRepository = studentRepository;
    }
    // Create a conversation with another student.
    // If one already exists, return the existing conversation.
    @PostMapping("/{otherStudentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResponse createConversation(
            @PathVariable Long otherStudentId,
            Authentication authentication) {

        // Identify the logged-in student using their JWT email.
        String email = authentication.getName();
        Student currentStudent = studentRepository.findByEmail(email);

        if (currentStudent == null) {
            throw new ResourceNotFoundException("Authenticated student not found.");
        }

        // Create or retrieve the conversation.
        Conversation conversation = messagingService.createConversation(
                currentStudent.getUserId(),
                otherStudentId
        );

        return new ConversationResponse(conversation);
    }

    // Retrieve all conversations belonging to the logged-in student.
    @GetMapping
    public List<ConversationResponse> getMyConversations(
            Authentication authentication) {

        String email = authentication.getName();

        Student currentStudent = studentRepository.findByEmail(email);

        if (currentStudent == null) {
            throw new ResourceNotFoundException("Authenticated student not found.");
        }

        List<Conversation> conversations =
                messagingService.getStudentConversations(currentStudent.getUserId());

        return conversations.stream()
                .map(ConversationResponse::new)
                .toList();
    }
}

