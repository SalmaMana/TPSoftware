package ma.um6p.compusconnect;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class CreateSessionRequest {

    @NotNull
    private Long receiverId;

    @NotNull
    @Future(message = "Session date must be in the future.")
    private LocalDateTime scheduledAt;

    @NotBlank(message = "Session details cannot be empty.")
    @Size(max = 2000)
    private String details;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}