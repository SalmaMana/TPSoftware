package ma.um6p.compusconnect;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id", "reviewer_id"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // Session associated with the review
    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // Student who writes the review
    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Student reviewer;

    // Student who receives the review
    @ManyToOne(optional = false)
    @JoinColumn(name = "reviewed_student_id", nullable = false)
    private Student reviewedStudent;

    // Rating between 1 and 5
    @Column(nullable = false)
    private Integer rating;

    // Review comment
    @Column(length = 1000)
    private String comment;

    public Review() {
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Student getReviewer() {
        return reviewer;
    }

    public void setReviewer(Student reviewer) {
        this.reviewer = reviewer;
    }

    public Student getReviewedStudent() {
        return reviewedStudent;
    }

    public void setReviewedStudent(Student reviewedStudent) {
        this.reviewedStudent = reviewedStudent;
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