
package ma.um6p.compusconnect;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/*
 * Service responsible for the business logic
 * related to posts on CompusConnect.
 */
@Service
public class PostService {

    // Repository used to manage posts in MySQL.
    private final PostRepository postRepository;

    // Repository used to retrieve students.
    private final StudentRepository studentRepository;

    /*
     * Constructor injection.
     */
    public PostService(PostRepository postRepository,
                       StudentRepository studentRepository) {

        this.postRepository = postRepository;
        this.studentRepository = studentRepository;
    }

    /*
     * Creates a new post for the authenticated student.
     */
    public Post createPost(CreatePostRequest request,
                           String authenticatedEmail) {

        // Find the student using the authenticated email.
        Student student =
                studentRepository.findByEmail(authenticatedEmail);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        // Create the post entity.
        Post post = new Post();

        // Copy the information from the request.
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPostType(request.getPostType());

        // Save the new category.
        post.setCategory(request.getCategory().trim());

        // Associate the post with its author.
        post.setAuthor(student);

        // Save the post in the database.
        return postRepository.save(post);
    }

    /*
     * Retrieves a post by its ID.
     */
    public PostResponse getPostById(Long postId) {

        Optional<Post> optionalPost =
                postRepository.findById(postId);

        if (optionalPost.isEmpty()) {
            throw new ResourceNotFoundException("Post not found");
        }

        return new PostResponse(optionalPost.get());
    }

    /*
     * Updates a post.
     * Only its author can modify it.
     */
    public PostResponse updatePost(
            Long postId,
            UpdatePostRequest request,
            String authenticatedEmail) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        // Verify that the authenticated student owns the post.
        if (!post.getAuthor().getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedActionException(
                    "You are not allowed to update this post");
        }

        // Update the post information.
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPostType(request.getPostType());

        // Update the category.
        post.setCategory(request.getCategory().trim());

        // Save the changes.
        Post updatedPost = postRepository.save(post);

        return new PostResponse(updatedPost);
    }

    /*
     * Deletes a post.
     * Only its author can delete it.
     */
    public void deletePost(
            Long postId,
            String authenticatedEmail) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        // Verify ownership.
        if (!post.getAuthor().getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedActionException(
                    "You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    /*
     * Retrieves posts of a specific type (NEED or HAVE).
     */
    public List<PostResponse> getPostsByType(PostType postType) {

        List<Post> posts =
                postRepository.findByPostTypeOrderByCreationDateDesc(
                        postType);

        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /*
     * Searches for posts containing a keyword
     * in their title or description.
     */
    public List<PostResponse> searchPosts(String keyword) {

        List<Post> posts = postRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword);

        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /*
     * Retrieves all posts, newest first.
     */
    public List<PostResponse> getAllPosts() {

        List<Post> posts =
                postRepository.findAllByOrderByCreationDateDesc();

        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /*
     * NEW: Combined search and filtering.
     *
     * Each parameter is optional.
     *
     * Examples:
     * type = HAVE
     * category = Programming
     * keyword = Java
     *
     * If a parameter is null, the repository ignores
     * that particular filter.
     */
    public List<PostResponse> searchWithFilters(
            PostType type,
            String category,
            String keyword) {

        // Treat blank category and keyword as absent filters.
        if (category != null && category.isBlank()) {
            category = null;
        }

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        // Remove extra spaces around the search values.
        if (category != null) {
            category = category.trim();
        }

        if (keyword != null) {
            keyword = keyword.trim();
        }

        // Execute the combined search query.
        List<Post> posts = postRepository.searchWithFilters(
                type, category, keyword);

        // Convert the results into response DTOs.
        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
