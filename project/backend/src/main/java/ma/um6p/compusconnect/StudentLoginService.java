package ma.um6p.compusconnect;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentLoginService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Constructor injection: Spring provides these three services automatically
    public StudentLoginService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public StudentLoginResponse login(StudentLoginRequest request) {

        // 1. Find the student using the email provided in the login request
        Student student = studentRepository.findByEmail(request.getEmail());

        // 2. If the email doesn't exist, reject the login
        if (student == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        // 3. Compare the entered password with the stored BCrypt hash
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                student.getPasswordHash()
        );

        // 4. If the password is incorrect, reject the login
        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        // 5. Generate a real JWT token after successful authentication
        String token = jwtService.generateToken(student.getEmail());

        // 6. Return the token and student information
        return new StudentLoginResponse(
                token,
                student.getUserId(),
                student.getFullName(),
                student.getEmail(),
                student.getUniversityId(),
                student.getMajor()
        );
    }
}