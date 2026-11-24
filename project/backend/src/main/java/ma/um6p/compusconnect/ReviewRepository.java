package ma.um6p.compusconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Check if the student has already reviewed this session
    boolean existsBySession_SessionIdAndReviewer_UserId(
            Long sessionId, Long reviewerId);

    // Retrieve all reviews received by a student
    List<Review> findByReviewedStudent_UserId(Long studentId);

    // Calculate the average rating of a student
    @Query("SELECT AVG(r.rating) FROM Review r " +
            "WHERE r.reviewedStudent.userId = :studentId")
    Double calculateAverageRating(@Param("studentId") Long studentId);
}