package ma.um6p.compusconnect;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 * Entity representing a post published by a student
 * on CompusConnect.
 *
 * A post can represent either:
 * - Something a student NEEDS.
 * - Something a student HAS to offer.
 *
 * This class is mapped to a table in the MySQL database.
 */
@Entity
public class Post {

    /*
     * Primary key of the post.
     *
     * @Id identifies this field as the primary key.
     * @GeneratedValue allows the database to generate
     * a unique ID automatically when a post is created.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    // Short title describing the student's need or offer.
    private String title;
    // Detailed explanation of what the student needs or offers.
    @Column(columnDefinition = "TEXT")
    private String description;
    /*
     * Defines whether this post represents a NEED or a HAVE.
     *
     * EnumType.STRING stores the enum name as text in the database
     * (for example, "NEED" or "HAVE") instead of its numeric position.
     */
    @Enumerated(EnumType.STRING)
    private PostType postType;
    /*
     * The date and time when the post was created.
     */
    private LocalDateTime creationDate;
    /*
     * Many posts can belong to the same student.
     *
     * Each post has only one author.
     * The author is linked to the existing Student entity.
     *
     * This relationship is stored using a foreign key column
     * named author_id in the post table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Student author;
    /*
     * Automatically sets the creation date and time
     * immediately before the entity is first persisted.
     */
    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    /*
     * Getters and setters allow other classes to access
     * and modify the fields while keeping them private.
     */

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
