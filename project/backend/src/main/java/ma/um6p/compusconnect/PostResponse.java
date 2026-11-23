
package ma.um6p.compusconnect;

import java.time.LocalDateTime;

/*
 * DTO representing the information returned
 * to the frontend when retrieving or creating a post.
 *
 * It exposes only the necessary information,
 * without exposing the entire Post or Student entities.
 */
public class PostResponse {

    // Unique identifier of the post.
    private Long postId;

    // Title of the post.
    private String title;

    // Detailed description of the post.
    private String description;

    // Indicates whether the post is a NEED or HAVE.
    private PostType postType;

    /*
     * NEW: Category of the post.
     *
     * Examples: Programming, Mathematics, Languages.
     * Existing posts may initially have a null category.
     */
    private String category;

    // Date and time when the post was created.
    private LocalDateTime creationDate;

    // Name of the student who published the post.
    private String authorName;

    /*
     * Constructor used to convert a Post entity
     * into a response DTO.
     */
    public PostResponse(Post post) {

        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.postType = post.getPostType();

        // NEW: Include the post category in the response.
        this.category = post.getCategory();

        this.creationDate = post.getCreationDate();

        // Retrieve the name of the student who created the post.
        this.authorName = post.getAuthor().getFullName();
    }

    // =========================
    // GETTERS
    // =========================

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

    // Returns the category of the post.
    public String getCategory() {
        return category;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getAuthorName() {
        return authorName;
    }
}
