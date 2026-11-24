package ma.um6p.compusconnect;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller responsible for handling review-related requests
@RestController

// Base URL for all review endpoints
@RequestMapping("/api/reviews")
public class ReviewController {

    // Service responsible for review business logic
    private final ReviewService reviewService;

    // Repository used to identify the authenticated student
    private final StudentRepository studentRepository;

    // Constructor injection:
    // Spring automatically injects the service and repository
    public ReviewController(
            ReviewService reviewService,
            StudentRepository studentRepository) {

        this.reviewService = reviewService;
        this.studentRepository = studentRepository;
    }

    // POST /api/reviews
    // Allows a student to review another student
    // after a completed session
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {

        // Retrieve the ID of the currently authenticated student
        Long reviewerId = getAuthenticatedStudentId(authentication);

        // Call the service to validate and create the review.
        // The service also updates the student's average rating.
        ReviewResponse response =
                reviewService.createReview(reviewerId, request);

        // Return HTTP 201 Created with the created review
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET /api/reviews/student/{studentId}
    // Retrieve all reviews received by a specific student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ReviewResponse>> getStudentReviews(
            @PathVariable Long studentId) {

        // Call the service to retrieve the student's reviews
        List<ReviewResponse> reviews =
                reviewService.getStudentReviews(studentId);

        // Return HTTP 200 OK with the list of reviews
        return ResponseEntity.ok(reviews);
    }

    // Helper method that identifies the currently logged-in student
    // Helper method to identify the currently authenticated student
    private Long getAuthenticatedStudentId(
            Authentication authentication) {

        // Extract the student's email from the JWT authentication
        String email = authentication.getName();

        // Retrieve the student from the database using their email
        Student student = studentRepository.findByEmail(email);

        // Verify that the student exists
        if (student == null) {
            throw new ResourceNotFoundException("Student not found");
        }

        // Return the authenticated student's unique ID
        return student.getUserId();
    }
}