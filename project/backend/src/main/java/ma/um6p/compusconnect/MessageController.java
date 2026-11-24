
package ma.um6p.compusconnect;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {
    private final MessageService messageService;
    private final StudentRepository studentRepository;
    // Constructor injection
    public MessageController(
            MessageService messageService,
            StudentRepository studentRepository) {
        this.messageService = messageService;
        this.studentRepository = studentRepository;
    }
    // Send a message in a conversation.
    @PostMapping
    public MessageResponse sendMessage(
            @PathVariable Long conversationId,
            @RequestBody SendMessageRequest request,
            Authentication authentication) {
        // Identify the sender using their authenticated JWT email.
        String email = authentication.getName();
        Student currentStudent = studentRepository.findByEmail(email);
        if (currentStudent == null) {
            throw new ResourceNotFoundException("Authenticated student not found.");
        }
        Message message = messageService.sendMessage(
                conversationId,
                currentStudent.getUserId(),
                request.getContent()
        );
        return new MessageResponse(message);
    }
    // Retrieve all messages in a conversation.
    @GetMapping
    public List<MessageResponse> getMessages(
            @PathVariable Long conversationId,
            Authentication authentication) {

        String email = authentication.getName();

        Student currentStudent = studentRepository.findByEmail(email);

        if (currentStudent == null) {
            throw new ResourceNotFoundException("Authenticated student not found.");
        }

        List<Message> messages = messageService.getConversationMessages(
                conversationId,
                currentStudent.getUserId()
        );

        return messages.stream()
                .map(MessageResponse::new)
                .toList();
    }
}
