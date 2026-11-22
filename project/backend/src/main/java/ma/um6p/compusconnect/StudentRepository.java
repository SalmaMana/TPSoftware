package ma.um6p.compusconnect;

// Import JpaRepository, which provides ready-made database operations
import org.springframework.data.jpa.repository.JpaRepository;
// This interface defines database operations for Student entities.
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Checks whether a student with this email already exists.
    boolean existsByEmail(String email);
    // Checks whether a student with this university ID already exists.
    boolean existsByUniversityId(String universityId);
}
