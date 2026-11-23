
package ma.um6p.compusconnect;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/*
 * Service responsible for matching complementary posts.
 *
 * A NEED post is matched with HAVE posts, and vice versa.
 *
 * Matching rules:
 * 1. Posts must have opposite types.
 * 2. Posts must belong to different students.
 * 3. If both categories are specified, they must match.
 * 4. Posts must share at least one meaningful keyword.
 *
 * Similarity is calculated using Jaccard similarity.
 * No AI or machine learning is used.
 */
@Service
public class MatchingService {

    // Repository used to retrieve posts from MySQL.
    private final PostRepository postRepository;

    // Constructor injection.
    public MatchingService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /*
     * Finds potential matches for a specific post.
     *
     * @param postId ID of the post we want to match
     * @return matching posts sorted by similarity score
     */
    @Transactional(readOnly = true)
    public List<MatchingResponse> findMatches(Long postId) {

        // STEP 1: Find the selected post.
        Post selectedPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post not found"));

        // STEP 2: Determine the opposite post type.
        PostType oppositeType;

        if (selectedPost.getPostType() == PostType.NEED) {
            oppositeType = PostType.HAVE;
        } else {
            oppositeType = PostType.NEED;
        }

        // STEP 3: Retrieve opposite-type posts from other students.
        Long authorId = selectedPost.getAuthor().getUserId();

        List<Post> candidates =
                postRepository.findByPostTypeAndAuthor_UserIdNot(
                        oppositeType, authorId);

        // List that will contain compatible post pairs.
        List<MatchingResponse> matches = new ArrayList<>();

        // STEP 4: Compare the selected post with each candidate.
        for (Post candidate : candidates) {

            /*
             * NEW: Check category compatibility first.
             *
             * If both posts have categories, they must match.
             * The comparison ignores uppercase/lowercase
             * differences and extra spaces.
             *
             * If one category is missing, allow matching
             * to preserve compatibility with older posts.
             */
            if (!categoriesMatch(
                    selectedPost.getCategory(),
                    candidate.getCategory())) {
                continue;
            }

            // Calculate keyword similarity.
            double score = calculateSimilarity(
                    selectedPost, candidate);

            // Keep only posts sharing meaningful keywords.
            if (score > 0) {

                // Ensure the NEED post always appears first.
                Post needPost;
                Post havePost;

                if (selectedPost.getPostType() == PostType.NEED) {
                    needPost = selectedPost;
                    havePost = candidate;
                } else {
                    needPost = candidate;
                    havePost = selectedPost;
                }

                // Convert the entities into response DTOs.
                matches.add(new MatchingResponse(
                        new PostResponse(needPost),
                        new PostResponse(havePost),
                        score));
            }
        }

        // STEP 5: Sort matches by score, highest first.
        matches.sort(
                Comparator.comparingDouble(
                        MatchingResponse::getMatchingScore
                ).reversed());

        return matches;
    }

    /*
     * NEW: Checks whether two categories are compatible.
     *
     * Examples:
     * Programming + Programming -> true
     * Programming + programming -> true
     * Programming + Mathematics -> false
     * null + Programming        -> true
     *
     * Missing categories are allowed because some
     * older posts were created before categories existed.
     */
    private boolean categoriesMatch(
            String firstCategory,
            String secondCategory) {

        // Allow matching if either category is missing.
        if (firstCategory == null || firstCategory.isBlank()
                || secondCategory == null || secondCategory.isBlank()) {
            return true;
        }

        // Otherwise, require the same category.
        return firstCategory.trim()
                .equalsIgnoreCase(secondCategory.trim());
    }

    /*
     * Calculates keyword similarity using Jaccard similarity.
     *
     * Formula:
     *
     * score = common distinct words / total distinct words * 100
     */
    private double calculateSimilarity(Post first, Post second) {

        // Combine the title and description of each post.
        String firstText =
                first.getTitle() + " " + first.getDescription();

        String secondText =
                second.getTitle() + " " + second.getDescription();

        // Extract meaningful keywords.
        Set<String> firstWords = extractKeywords(firstText);
        Set<String> secondWords = extractKeywords(secondText);

        // If either post has no usable words, return zero.
        if (firstWords.isEmpty() || secondWords.isEmpty()) {
            return 0;
        }

        // Intersection: words shared by both posts.
        Set<String> commonWords = new HashSet<>(firstWords);
        commonWords.retainAll(secondWords);

        // Union: all distinct words in either post.
        Set<String> allWords = new HashSet<>(firstWords);
        allWords.addAll(secondWords);

        // Calculate the similarity percentage.
        double score =
                (double) commonWords.size() / allWords.size() * 100;

        // Round to two decimal places.
        return Math.round(score * 100.0) / 100.0;
    }

    /*
     * Extracts meaningful keywords from a text.
     *
     * Example:
     * "I need help with Java programming!"
     *
     * becomes:
     * ["need", "help", "java", "programming"]
     */
    private Set<String> extractKeywords(String text) {

        // Convert to lowercase and split at non-alphanumeric characters.
        String[] words = text.toLowerCase(Locale.ROOT)
                .split("[^\\p{L}\\p{N}]+");

        // Common words that do not help identify a match.
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "a", "an", "the", "and", "or", "to", "for",
                "of", "in", "on", "at", "is", "are", "i",
                "you", "we", "with", "my", "your"
        ));

        Set<String> keywords = new HashSet<>();

        for (String word : words) {

            // Ignore empty words and stop words.
            if (!word.isBlank() && !stopWords.contains(word)) {
                keywords.add(word);
            }
        }

        return keywords;
    }
}
