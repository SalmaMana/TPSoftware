package ma.um6p.compusconnect;

import jakarta.validation.constraints.*;

public class CreateReviewRequest {

    // Session associated with the review
    @NotNull(message = "Session ID is required")
    private Long sessionId;

    // Rating must be between 1 and 5
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    // Optional comment with a maximum of 1000 characters
    @Size(max = 1000, message = "Comment is too long")
    private String comment;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}