
package ma.um6p.compusconnect;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 * Repository responsible for database operations
 * involving Post entities.
 *
 * JpaRepository already provides:
 * save(), findAll(), findById(), delete(), etc.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /*
     * Retrieves posts of a particular type (NEED or HAVE),
     * ordered from newest to oldest.
     *
     * Used by Salma's existing type-filtering endpoint.
     */
    List<Post> findByPostTypeOrderByCreationDateDesc(
            PostType postType
    );

    /*
     * Searches for a keyword in the title or description.
     * The search ignores uppercase/lowercase differences.
     *
     * Preserves Salma's existing keyword search.
     */
    List<Post> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword
    );

    /*
     * Retrieves all posts ordered from newest to oldest.
     */
    List<Post> findAllByOrderByCreationDateDesc();

    /*
     * Retrieves posts of a given type while excluding
     * posts created by the specified student.
     *
     * Used by our Step 7 matching algorithm.
     */
    List<Post> findByPostTypeAndAuthor_UserIdNot(
            PostType postType,
            Long userId
    );

    /*
     * NEW: Combined search and filtering.
     *
     * Students can filter posts using:
     * 1. Post type: NEED or HAVE
     * 2. Category: Programming, Mathematics, etc.
     * 3. Keyword: searched in title or description
     *
     * Every filter is optional.
     *
     * If a parameter is null, that filter is ignored.
     *
     * LOWER() makes text comparisons case-insensitive.
     * LIKE allows partial keyword matches.
     */
    @Query("""
            SELECT p FROM Post p
            WHERE (:type IS NULL OR p.postType = :type)
              AND (:category IS NULL
                   OR LOWER(p.category) = LOWER(:category))
              AND (:keyword IS NULL
                   OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
            ORDER BY p.creationDate DESC
            """)
    List<Post> searchWithFilters(
            @Param("type") PostType type,
            @Param("category") String category,
            @Param("keyword") String keyword
    );
}
