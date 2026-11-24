package ma.um6p.compusconnect;

public class ReviewResponse {

    private Long reviewId;
    private Long sessionId;
    private Long reviewerId;
    private String reviewerName;
    private Long reviewedStudentId;
    private String reviewedStudentName;
    private Integer rating;
    private String comment;

    // Constructor
    public ReviewResponse(Review review) {
        this.reviewId = review.getReviewId();
        this.sessionId = review.getSession().getSessionId();
        this.reviewerId = review.getReviewer().getUserId();
        this.reviewerName = review.getReviewer().getFullName();
        this.reviewedStudentId = review.getReviewedStudent().getUserId();
        this.reviewedStudentName = review.getReviewedStudent().getFullName();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }

    // Getters
    public Long getReviewId() {
        return reviewId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public Long getReviewedStudentId() {
        return reviewedStudentId;
    }

    public String getReviewedStudentName() {
        return reviewedStudentName;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}