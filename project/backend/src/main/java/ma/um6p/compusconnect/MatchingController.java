
package ma.um6p.compusconnect;

import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 * STEP 7 — MATCHING CONTROLLER
 *
 * This controller exposes an API endpoint that allows
 * students to find complementary NEED and HAVE posts.
 *
 * It delegates the matching logic to MatchingService.
 */
@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    // Service responsible for finding compatible posts
    private final MatchingService matchingService;

    /*
     * Constructor injection:
     * Spring automatically provides the MatchingService.
     */
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /*
     * GET /api/matching/posts/{postId}
     *
     * Finds potential matches for a selected post.
     *
     * Example:
     * GET /api/matching/posts/1
     *
     * If post 1 is a NEED post, the service searches
     * for compatible HAVE posts from other students.
     *
     * If post 1 is a HAVE post, it searches for
     * compatible NEED posts from other students.
     *
     * Results are sorted by matching score,
     * from highest to lowest.
     */
    @GetMapping("/posts/{postId}")
    public List<MatchingResponse> getMatches(
            @PathVariable Long postId) {

        return matchingService.findMatches(postId);
    }
}
