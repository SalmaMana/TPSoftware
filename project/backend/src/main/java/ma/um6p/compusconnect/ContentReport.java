package ma.um6p.compusconnect;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ContentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student who submitted the report
    private Long reporterId;

    // Reported post, if applicable
    private Long postId;

    // Reported student
    private Long reportedStudentId;

    @Column(length = 1000)
    private String reason;

    // Preserve reported content even if the post is deleted
    @Column(columnDefinition = "TEXT")
    private String contentSnapshot;

    private LocalDateTime createdAt;

    // Indicates whether an admin has handled the report
    private boolean resolved;

    // ID of the administrator who resolved the report
    private Long resolvedBy;

    protected ContentReport() {
    }

    public ContentReport(Long reporterId, Post post,
                         Long studentId, String reason) {

        this.reporterId = reporterId;
        this.postId = post == null ? null : post.getPostId();
        this.reportedStudentId = studentId;
        this.reason = reason;

        this.contentSnapshot = post == null ? null :
                post.getTitle() + "\n" + post.getDescription();

        this.createdAt = LocalDateTime.now();
        this.resolved = false;
    }

    public Long getId() {
        return id;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getReportedStudentId() {
        return reportedStudentId;
    }

    public String getReason() {
        return reason;
    }

    public String getContentSnapshot() {
        return contentSnapshot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isResolved() {
        return resolved;
    }

    public Long getResolvedBy() {
        return resolvedBy;
    }

    // Mark the report as handled by an administrator
    public void resolve(Long adminId) {
        resolved = true;
        resolvedBy = adminId;
    }
}