
package ma.um6p.compusconnect;

/*
 * DTO representing a potential match between
 * two complementary posts.
 *
 * A match connects:
 * - One NEED post.
 * - One HAVE post.
 *
 * The matching score represents how similar
 * the content of the two posts is.
 */
public class MatchingResponse {

    // The post requesting something
    private PostResponse needPost;

    // The post offering something
    private PostResponse havePost;

    // Similarity score between 0 and 100
    private double matchingScore;

    // Constructor
    public MatchingResponse(
            PostResponse needPost,
            PostResponse havePost,
            double matchingScore) {

        this.needPost = needPost;
        this.havePost = havePost;
        this.matchingScore = matchingScore;
    }

    // Return the NEED post
    public PostResponse getNeedPost() {
        return needPost;
    }

    // Return the HAVE post
    public PostResponse getHavePost() {
        return havePost;
    }

    // Return the similarity score
    public double getMatchingScore() {
        return matchingScore;
    }
}
