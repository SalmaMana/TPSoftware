package ma.um6p.compusconnect;

// Import Spring's service annotation
import org.springframework.stereotype.Service;
// Import password encoder
import org.springframework.security.crypto.password.PasswordEncoder;
// Import HTTP exception support
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
// Import transaction support
import org.springframework.transaction.annotation.Transactional;
// @Service tells Spring that this class contains business logic
// and should be managed as a Spring bean.
@Service
public class StudentService {
    // Repository used to communicate with the database
    private final StudentRepository studentRepository;
    // Used to hash passwords
    private final PasswordEncoder passwordEncoder;
    // Constructor injection:
    // Spring automatically provides the required objects.
    public StudentService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // This method handles the registration logic.
    // @Transactional ensures the database operation
    // is executed within a transaction.
    @Transactional
    public StudentRegistrationResponse registerStudent(
            StudentRegistrationRequest request) {
        // Check whether the email is already registered.
        if (studentRepository.existsByEmail(request.getEmail())) {
            // If it exists, reject the registration.
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered");
        }
        // Check whether the university ID is already used.
        if (studentRepository.existsByUniversityId(
                request.getUniversityId())) {
            // If it exists, reject the registration.
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "University ID is already registered");
        }
        // Create a new Student object.
        Student student = new Student();
        // Set the information inherited from User.
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        // Hash the password before storing it.
        student.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));
        // Set the Student-specific information.
        student.setUniversityId(request.getUniversityId());
        student.setMajor(request.getMajor());
        // Initial rating: no reviews exist yet.
        student.setAverageRating(0.0);
        // Save the student in the database.
        Student savedStudent = studentRepository.save(student);
        // Return only the information intended for the frontend.
        return new StudentRegistrationResponse(
                savedStudent.getUserId(),
                savedStudent.getFullName(),
                savedStudent.getEmail(),
                savedStudent.getUniversityId(),
                savedStudent.getMajor());
    }
}
