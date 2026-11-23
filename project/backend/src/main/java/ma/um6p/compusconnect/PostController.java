
package ma.um6p.compusconnect;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Controller responsible for handling HTTP requests
 * related to posts on CompusConnect.
 *
 * It receives requests from the frontend,
 * delegates business logic to PostService,
 * and returns the results.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    // Service responsible for post operations.
    private final PostService postService;

    /*
     * Constructor injection.
     * Spring automatically provides PostService.
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /*
     * Creates a new post for the authenticated student.
     *
     * POST /api/posts
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(
            @Valid @RequestBody CreatePostRequest request,
            Authentication authentication) {

        // Identify the currently authenticated student.
        String authenticatedEmail = authentication.getName();

        // Create and save the post.
        Post createdPost = postService.createPost(
                request, authenticatedEmail);

        // Return the post information to the frontend.
        return new PostResponse(createdPost);
    }

    /*
     * Retrieves all posts, ordered from newest to oldest.
     *
     * GET /api/posts
     */
    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    /*
     * Retrieves one post by its ID.
     *
     * GET /api/posts/5
     */
    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    /*
     * Updates a post.
     * Only the author is allowed to update it.
     *
     * PUT /api/posts/5
     */
    @PutMapping("/{id}")
    public PostResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            Authentication authentication) {

        String authenticatedEmail = authentication.getName();

        return postService.updatePost(
                id, request, authenticatedEmail);
    }

    /*
     * Deletes a post.
     * Only the author is allowed to delete it.
     *
     * DELETE /api/posts/5
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long id,
            Authentication authentication) {

        String authenticatedEmail = authentication.getName();

        postService.deletePost(id, authenticatedEmail);
    }

    /*
     * Existing endpoint: filters posts by type.
     *
     * GET /api/posts/filter?type=NEED
     * GET /api/posts/filter?type=HAVE
     */
    @GetMapping("/filter")
    public List<PostResponse> getPostsByType(
            @RequestParam PostType type) {

        return postService.getPostsByType(type);
    }

    /*
     * Existing endpoint: searches posts by keyword.
     *
     * GET /api/posts/search?keyword=Java
     */
    @GetMapping("/search")
    public List<PostResponse> searchPosts(
            @RequestParam String keyword) {

        return postService.searchPosts(keyword);
    }

    /*
     * NEW: Combined filtering endpoint.
     *
     * Students can filter posts by:
     * - Type: NEED or HAVE
     * - Category: Programming, Mathematics, etc.
     * - Keyword: searched in title or description
     *
     * All three parameters are optional.
     *
     * Examples:
     *
     * GET /api/posts/search-filters?category=Programming
     *
     * GET /api/posts/search-filters?type=HAVE
     *
     * GET /api/posts/search-filters?keyword=Java
     *
     * GET /api/posts/search-filters?type=HAVE&category=Programming&keyword=Java
     */
    @GetMapping("/search-filters")
    public List<PostResponse> searchWithFilters(
            @RequestParam(required = false) PostType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {

        return postService.searchWithFilters(
                type, category, keyword);
    }
}
