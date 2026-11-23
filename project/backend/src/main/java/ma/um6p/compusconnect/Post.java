
package ma.um6p.compusconnect;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 * Entity representing a post published by a student.
 *
 * A post can represent:
 * - Something a student NEEDS.
 * - Something a student HAS to offer.
 *
 * This entity is stored in the MySQL database.
 */
@Entity
public class Post {

    /*
     * Unique identifier of the post.
     * The database generates it automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    // Short title describing the need or offer.
    private String title;

    // Detailed description of the post.
    @Column(columnDefinition = "TEXT")
    private String description;

    /*
     * Type of post: NEED or HAVE.
     * EnumType.STRING stores the name in MySQL.
     */
    @Enumerated(EnumType.STRING)
    private PostType postType;

    /*
     * Category of the post.
     *
     * Examples:
     * Programming, Mathematics, Languages.
     *
     * Used to filter posts by category.
     *
     * Existing posts may initially have a null category.
     */
    @Column
    private String category;

    // Date and time when the post was created.
    private LocalDateTime creationDate;

    /*
     * Each post belongs to one student.
     * A student can publish multiple posts.
     *
     * The author_id column is a foreign key
     * referencing the student who created the post.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Student author;

    /*
     * Automatically assigns the creation date
     * when a new post is saved for the first time.
     */
    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    // Getter for the new category attribute.
    public String getCategory() {
        return category;
    }

    // Setter for the new category attribute.
    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Student getAuthor() {
        return author;
    }

    public void setAuthor(Student author) {
        this.author = author;
    }
}
