package ma.um6p.compusconnect;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repository responsible for database operations related to Post entities.
 * JpaRepository already provides methods such as findAll(), findById(),
 * save(), delete() and more.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository already provides findAll(), which retrieves all posts.
    /*
     * Retrieves posts matching the specified type (NEED or HAVE).
     * Posts are ordered from newest to oldest.
     */
    List<Post> findByPostTypeOrderByCreationDateDesc(PostType postType);
    /*
     * Searches for a keyword in either the post title or description.
     * The search is case-insensitive.
     */
    List<Post> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword);
    /*
     * Retrieves all posts ordered by creation date,
     * starting with the newest.
     */
    List<Post> findAllByOrderByCreationDateDesc();
}