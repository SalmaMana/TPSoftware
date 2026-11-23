
package ma.um6p.compusconnect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * DTO containing the information needed
 * to update an existing post.
 *
 * A student can update:
 * - Title
 * - Description
 * - Post type (NEED or HAVE)
 * - Category
 */
public class UpdatePostRequest {

    // New title of the post.
    @NotBlank(message = "Title is required")
    private String title;

    // New description of the post.
    @NotBlank(message = "Description is required")
    private String description;

    // New post type: NEED or HAVE.
    @NotNull(message = "Post type is required")
    private PostType postType;

    // NEW: Category of the post.
    @NotBlank(message = "Category is required")
    private String category;

    // =========================
    // GETTERS AND SETTERS
    // =========================

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

    // Returns the category received from the frontend.
    public String getCategory() {
        return category;
    }

    // Allows Spring to populate the category field.
    public void setCategory(String category) {
        this.category = category;
    }
}
