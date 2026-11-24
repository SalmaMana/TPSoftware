
package ma.um6p.compusconnect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/*
 * DTO containing the information required
 * to send a new message.
 */
public class SendMessageRequest {
    // Message content cannot be empty.
    @NotBlank(message = "Message content cannot be empty.")
    @Size(max = 5000, message = "Message cannot exceed 5000 characters.")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
