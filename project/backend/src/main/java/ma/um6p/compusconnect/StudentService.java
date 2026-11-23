
package ma.um6p.compusconnect;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    // Repository used to communicate with the database
    private final StudentRepository studentRepository;

    // Used to hash passwords during registration
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public StudentService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // STEP 5.0: Register a new student
    @Transactional
    public StudentRegistrationResponse registerStudent(
            StudentRegistrationRequest request) {

        // Check whether the email is already registered
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered");
        }

        // Check whether the university ID is already registered
        if (studentRepository.existsByUniversityId(
                request.getUniversityId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "University ID is already registered");
        }

        // Create a new student
        Student student = new Student();

        // Set information inherited from User
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());

        // Hash the password before saving
        student.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        // Set student-specific information
        student.setUniversityId(request.getUniversityId());
        student.setMajor(request.getMajor());
        student.setAverageRating(0.0);

        // Save the student in MySQL
        Student savedStudent = studentRepository.save(student);

        // Return the registration response
        return new StudentRegistrationResponse(
                savedStudent.getUserId(),
                savedStudent.getFullName(),
                savedStudent.getEmail(),
                savedStudent.getUniversityId(),
                savedStudent.getMajor());
    }

    // STEP 5.2: View the logged-in student's profile
    @Transactional(readOnly = true)
    public StudentProfileResponse getMyProfile(String email) {

        // Find the logged-in student using their email
        Student student = studentRepository.findByEmail(email);

        // Return 404 if the student does not exist
        if (student == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found");
        }

        // Return profile information without the password
        return new StudentProfileResponse(
                student.getUserId(),
                student.getFullName(),
                student.getEmail(),
                student.getUniversityId(),
                student.getMajor(),
                student.getAverageRating());
    }

    // STEP 5.3: Edit the logged-in student's profile
    @Transactional
    public StudentProfileResponse updateMyProfile(
            String email,
            UpdateStudentProfileRequest request) {

        // Find the student who is currently logged in
        Student student = studentRepository.findByEmail(email);

        // Return 404 if the student does not exist
        if (student == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found");
        }

        // Check that the new full name is not empty
        if (request.getFullName() == null ||
                request.getFullName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Full name cannot be empty");
        }

        // Check that the new major is not empty
        if (request.getMajor() == null ||
                request.getMajor().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Major cannot be empty");
        }

        // Update only the fields that students are allowed to edit
        student.setFullName(request.getFullName().trim());
        student.setMajor(request.getMajor().trim());

        // Save the updated student in MySQL
        Student savedStudent = studentRepository.save(student);

        // Return the updated profile
        return new StudentProfileResponse(
                savedStudent.getUserId(),
                savedStudent.getFullName(),
                savedStudent.getEmail(),
                savedStudent.getUniversityId(),
                savedStudent.getMajor(),
                savedStudent.getAverageRating());
    }
}
