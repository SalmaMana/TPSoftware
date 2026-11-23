package ma.um6p.compusconnect;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import java.util.Optional;

/*
 * Service responsible for the business logic
 * related to posts on CompusConnect.
 *
 * It connects the controller to the repository
 * and processes operations involving posts.
 */
@Service
public class PostService {
    /*
     * Repository used to save, retrieve and manage posts
     * in the database.
     */
    private final PostRepository postRepository;

    /*
     * Repository used to retrieve student information.
     *
     * We need it to associate each post with
     * the student who created it.
     */
    private final StudentRepository studentRepository;

    /*
     * Constructor injection.
     *
     * Spring automatically provides the required
     * repository objects when creating PostService.
     */
    public PostService(PostRepository postRepository,
                       StudentRepository studentRepository) {

        this.postRepository = postRepository;
        this.studentRepository = studentRepository;
    }

    /*
     * Creates a new post for the authenticated student.
     *
     * The email is obtained from the authenticated user,
     * not from the request body.
     *
     * Steps:
     * 1. Find the student using their authenticated email.
     * 2. Create a new Post object.
     * 3. Copy the data from the request DTO.
     * 4. Associate the post with its author.
     * 5. Save the post in the database.
     */
    public Post createPost(CreatePostRequest request,
                           String authenticatedEmail) {

        /*
         * Retrieve the student using the email of the authenticated user.
         *
         * If no student is found, findByEmail returns null
         */
        Student student = studentRepository.findByEmail(authenticatedEmail);
        /*
         * Verify that the student exists before creating the post.
         */
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        /*
         * Create a new Post object.
         *
         * This object will represent the post
         * that we want to store in the database.
         */
        Post post = new Post();
        /*
         * Copy the information received from the DTO
         * into the new Post entity.
         */
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPostType(request.getPostType());
        /*
         * Associate the post with the student
         * who is currently authenticated.
         */
        post.setAuthor(student);

        /*
         * Save the post in the database.
         *
         * The database generates the post ID.
         * The @PrePersist method in Post sets the creation date.
         *
         * Return the saved Post entity.
         */
        return postRepository.save(post);
    }

    /*
     * Retrieves one post using its ID.
     * If the post does not exist, a 404 error will be returned.
     */
    public PostResponse getPostById(Long postId) {
        // Search for the post using its ID.
        Optional<Post> optionalPost = postRepository.findById(postId);

        // Throw a specific exception if the post doesn't exist.
        if (optionalPost.isEmpty()) {
            throw new ResourceNotFoundException("Post not found");
        }

        // Retrieve the Post object from Optional.
        Post post = optionalPost.get();

        // Convert the entity into a response DTO.
        return new PostResponse(post);
    }



    /*
     * Updates an existing post.
     * Only its author can modify it.
     */
    public PostResponse updatePost(
            Long postId,
            UpdatePostRequest request,
            String authenticatedEmail) {
        // Find the post or report that it doesn't exist.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        // Verify that the authenticated student owns this post.
        if (!post.getAuthor().getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedActionException(
                    "You are not allowed to update this post");
        }
        // Apply the new information.
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPostType(request.getPostType());
        // Save and return the updated post.
        Post updatedPost = postRepository.save(post);
        return new PostResponse(updatedPost);
    }

    /*
     * Deletes an existing post.
     * Only its author can delete it.
     */
    public void deletePost(Long postId, String authenticatedEmail) {
        // Find the post or report that it doesn't exist.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        // Verify that the authenticated student owns this post.
        if (!post.getAuthor().getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedActionException(
                    "You are not allowed to delete this post");
        }
        // Delete the post from the database.
        postRepository.delete(post);
    }
    /*
     * Retrieves all posts of a specific type (NEED or HAVE),
     * ordered from newest to oldest.
     */
    public List<PostResponse> getPostsByType(PostType postType) {

        // Retrieve matching posts from the database.
        List<Post> posts =
                postRepository.findByPostTypeOrderByCreationDateDesc(postType);

        // Convert each Post entity into a PostResponse.
        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }


    /*
     * Searches for posts containing the given keyword
     * in their title or description.
     */
    public List<PostResponse> searchPosts(String keyword) {
        // Use the same keyword to search both fields.
        List<Post> posts =
                postRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                keyword, keyword);

        // Convert database entities into response objects.
        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /*
     * Retrieves all posts, ordered from newest to oldest.
     */
    public List<PostResponse> getAllPosts() {

        // Retrieve all posts in descending creation-date order.
        List<Post> posts = postRepository.findAllByOrderByCreationDateDesc();

        // Convert each Post entity into a PostResponse.
        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

}
