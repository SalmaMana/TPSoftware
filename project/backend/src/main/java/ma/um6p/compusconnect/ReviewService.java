package ma.um6p.compusconnect;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class ReviewService {

    // Repository used to manage reviews
    private final ReviewRepository reviewRepository;

    // Repository used to retrieve sessions
    private final SessionRepository sessionRepository;

    // Repository used to retrieve and update students
    private final StudentRepository studentRepository;

    // Constructor injection:
    // Spring automatically provides the required repositories
    public ReviewService(
            ReviewRepository reviewRepository,
            SessionRepository sessionRepository,
            StudentRepository studentRepository) {

        this.reviewRepository = reviewRepository;
        this.sessionRepository = sessionRepository;
        this.studentRepository = studentRepository;
    }

    // Submit a review after a completed session.
    // Transactional ensures that saving the review and updating
    // the average rating are performed as one operation.
    @Transactional
    public ReviewResponse createReview(
            Long reviewerId, CreateReviewRequest request) {

        // 1. Retrieve the student writing the review
        Student reviewer = studentRepository.findById(reviewerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        // 2. Retrieve the session associated with the review
        Session session = sessionRepository
                .findById(request.getSessionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Session not found"));

        // 3. Verify that the reviewer participated in the session
        boolean isProposer = session.getProposer()
                .getUserId().equals(reviewerId);

        boolean isReceiver = session.getReceiver()
                .getUserId().equals(reviewerId);

        // Reject students who did not participate in the session
        if (!isProposer && !isReceiver) {
            throw new UnauthorizedActionException(
                    "You cannot review a session you did not participate in");
        }

        // 4. Only completed sessions can receive reviews
        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Only completed sessions can be reviewed");
        }

        // 5. Identify the student receiving the review.
        // If the reviewer is the proposer, review the receiver.
        // Otherwise, review the proposer.
        Student reviewedStudent;

        if (isProposer) {
            reviewedStudent = session.getReceiver();
        } else {
            reviewedStudent = session.getProposer();
        }

        // 6. Prevent students from reviewing themselves
        if (reviewerId.equals(reviewedStudent.getUserId())) {
            throw new IllegalArgumentException(
                    "You cannot review yourself");
        }

        // 7. Prevent duplicate reviews.
        // Each participant can submit only one review per session.
        if (reviewRepository
                .existsBySession_SessionIdAndReviewer_UserId(
                        session.getSessionId(), reviewerId)) {

            throw new IllegalArgumentException(
                    "You have already reviewed this session");
        }

        // 8. Verify that the rating is between 1 and 5
        if (request.getRating() == null
                || request.getRating() < 1
                || request.getRating() > 5) {

            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5");
        }

        // 9. Check whether the comment contains inappropriate words
        if (containsInappropriateContent(request.getComment())) {
            throw new IllegalArgumentException(
                    "Review contains inappropriate content");
        }

        // 10. Create the review entity
        Review review = new Review();

        review.setSession(session);
        review.setReviewer(reviewer);
        review.setReviewedStudent(reviewedStudent);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // 11. Save the review in the database
        Review savedReview = reviewRepository.saveAndFlush(review);

        // 12. Recalculate the reviewed student's average rating
        Double averageRating = reviewRepository
                .calculateAverageRating(reviewedStudent.getUserId());

        // 13. Update the student's average rating
        reviewedStudent.setAverageRating(averageRating);

        // Save the updated student in the database
        studentRepository.save(reviewedStudent);

        // 14. Convert the saved entity into a response DTO
        return new ReviewResponse(savedReview);
    }

    // Retrieve all reviews received by a particular student
    @Transactional(readOnly = true)
    public List<ReviewResponse> getStudentReviews(Long studentId) {

        // Verify that the student exists
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found");
        }

        // Retrieve reviews and convert each entity into a response DTO
        return reviewRepository
                .findByReviewedStudent_UserId(studentId)
                .stream()
                .map(ReviewResponse::new)
                .toList();
    }

    // Helper method used to detect basic inappropriate content
    private boolean containsInappropriateContent(String comment) {

        // Comments are optional, so null or empty comments are allowed
        if (comment == null || comment.isBlank()) {
            return false;
        }

        // Convert to lowercase to make the verification
        // case-insensitive
        String normalizedComment = comment.toLowerCase(Locale.ROOT);

        // Basic list of prohibited words
        String[] prohibitedWords = {
                "idiot",
                "stupid",
                "fuck",
                "bitch",
                "asshole",
                "moron"
        };

        // Split the comment into individual words
        String[] words = normalizedComment.split("[^\\p{L}\\p{N}]+");

        // Check each word against the prohibited words
        for (String word : words) {
            for (String prohibitedWord : prohibitedWords) {
                if (word.equals(prohibitedWord)) {
                    return true;
                }
            }
        }

        // No prohibited words were detected
        return false;
    }
}