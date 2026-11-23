package ma.um6p.compusconnect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * DTO used when a student wants to create a new post.
 *
 * It defines the information the frontend is allowed
 * to send to the backend when creating a post.
 *
 * The author, post ID and creation date are not included
 * because the backend manages those elements itself.
 */
public class CreatePostRequest {
    /*
     * The title must not be null, empty or contain
     * only whitespace characters.
     */
    @NotBlank(message = "Title is required")
    private String title;
    /*
     * The description must not be null, empty or contain
     * only whitespace characters.
     */
    @NotBlank(message = "Description is required")
    private String description;

    /*
     * The post type must be specified.
     *
     * It must correspond to one of the values
     * defined in the PostType enum: NEED or HAVE.
     */
    @NotNull(message = "Post type is required")
    private PostType postType;

    /*
     * Getters allow other classes to read the values
     * received through this DTO.
     */

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PostType getPostType() {
        return postType;
    }

    /*
     * Setters allow Spring to populate this object
     * with the data received in the HTTP request.
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }
}
