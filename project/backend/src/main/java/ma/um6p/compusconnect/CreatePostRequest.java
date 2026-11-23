
package ma.um6p.compusconnect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * DTO used when a student creates a new post.
 *
 * The frontend sends the title, description,
 * post type and category.
 *
 * The backend manages the author, ID and creation date.
 */
public class CreatePostRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Post type is required")
    private PostType postType;

    // Category of the post (Programming, Mathematics, etc.).
    @NotBlank(message = "Category is required")
    private String category;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PostType getPostType() {
        return postType;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
