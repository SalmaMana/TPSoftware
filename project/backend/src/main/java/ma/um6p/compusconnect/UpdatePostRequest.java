
package ma.um6p.compusconnect;
// Used to validate the fields received in the update request.
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * DTO containing the information needed to update an existing post.
 */
public class UpdatePostRequest {
    // New title of the post.
    @NotBlank
    private String title;
    // New description of the post.
    @NotBlank
    private String description;
    // New type: NEED or HAVE.
    @NotNull
    private PostType postType;
    // Getters and setters allow the Service to access the fields.

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
}

