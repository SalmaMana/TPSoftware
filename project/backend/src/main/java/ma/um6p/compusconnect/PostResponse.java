package ma.um6p.compusconnect;

import java.time.LocalDateTime;

/*
 * DTO representing the information returned to the client
 * after retrieving or creating a post.
 *
 * It exposes only the information needed by the frontend,
 * without exposing the entire Post or Student entities.
 */
public class PostResponse {
    // Unique identifier of the post.
    private Long postId;
    // Title of the post.
    private String title;
    // Detailed description of the post.
    private String description;
    // Indicates whether the post is a NEED or a HAVE.
    private PostType postType;
    // Date and time when the post was created.
    private LocalDateTime creationDate;
    // Name of the student who published the post.
    private String authorName;

    /*
     * Constructor used to build a response
     * from a saved Post entity.
     */
    public PostResponse(Post post) {

        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.postType = post.getPostType();
        this.creationDate = post.getCreationDate();

        /*
         * Extract the author's name from the associated Student.
         */
        this.authorName = post.getAuthor().getFullName();
    }

    /*
     * Getters allow Jackson (Spring's JSON serialization)
     * and other classes to read the response fields.
     */

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PostType getPostType() {
        return postType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getAuthorName() {
        return authorName;
    }
}
