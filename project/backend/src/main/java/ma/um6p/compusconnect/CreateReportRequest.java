package ma.um6p.compusconnect;

import jakarta.validation.constraints.*;

public record CreateReportRequest(

        Long postId,

        Long studentId,

        @NotBlank(message = "Report reason is required")
        @Size(max = 1000)
        String reason

) {
}