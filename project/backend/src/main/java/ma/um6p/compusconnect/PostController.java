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
 * It receives requests from the frontend, delegates the
 * business logic to PostService, and returns responses.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    /*
     * Service responsible for processing post operations.
     */
    private final PostService postService;

    /*
     * Constructor injection.
     *
     * Spring provides the PostService instance automatically.
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /*
     * Endpoint used to create a new post.
     *
     * POST /api/posts
     *
     * @Valid triggers validation of the request DTO.
     * Authentication provides the identity of the
     * currently authenticated user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(
            @Valid @RequestBody CreatePostRequest request,
            Authentication authentication) {

        /*
         * Retrieve the email of the authenticated user.
         *
         * In our existing JWT configuration, authentication.getName()
         * represents the authenticated user's email.
         */
        String authenticatedEmail = authentication.getName();
        /*
         * Delegate the creation logic to the service.
         *
         * The service creates and saves the post.
         * We then convert the saved entity into a response DTO.
         */
        Post createdPost = postService.createPost(
                request, authenticatedEmail);

        return new PostResponse(createdPost);
    }
    /*
     * GET endpoint that retrieves all posts published on CompusConnect.
     * URL: GET /api/posts
     */
    @GetMapping
    public List<PostResponse> getAllPosts() {
        // Ask the Service to retrieve and convert all posts.
        return postService.getAllPosts();
    }

    /*
     * GET endpoint that retrieves one post using its ID.
     * Example URL: GET /api/posts/5
     */
    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        // Ask the Service to retrieve the post with the requested ID.
        return postService.getPostById(id);
    }
    /*
     * PUT endpoint that updates an existing post.
     * URL: PUT /api/posts/{id}
     */
    @PutMapping("/{id}")
    public PostResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            Authentication authentication) {
        // Get the email of the currently authenticated student.
        String authenticatedEmail = authentication.getName();
        // Ask the Service to update the post.
        // The Service also verifies that the student owns it.
        return postService.updatePost(id, request, authenticatedEmail);
    }

    /*
     * DELETE endpoint that removes an existing post.
     * URL: DELETE /api/posts/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long id,
            Authentication authentication) {

        // Get the email of the currently authenticated student.
        String authenticatedEmail = authentication.getName();

        // Ask the Service to delete the post.
        // The Service verifies that the student owns it.
        postService.deletePost(id, authenticatedEmail);
    }
    /*
     * Retrieves posts filtered by their type.
     *
     * Example:
     * GET /api/posts/filter?type=NEED
     * GET /api/posts/filter?type=HAVE
     */
    @GetMapping("/filter")
    public List<PostResponse> getPostsByType(
            @RequestParam PostType type) {
        return postService.getPostsByType(type);
    }

    /*
     * Searches posts by a keyword in their title or description.
     *
     * Example:
     * GET /api/posts/search?keyword=Java
     */
    @GetMapping("/search")
    public List<PostResponse> searchPosts(
            @RequestParam String keyword) {

        return postService.searchPosts(keyword);
    }


}


