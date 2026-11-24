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

    // Constructor injection
    public StudentLoginService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public StudentLoginResponse login(StudentLoginRequest request) {

        // Find the student using their email
        Student student = studentRepository.findByEmail(
                request.getEmail()
        );

        // Reject login if the account does not exist
        // or if the account is suspended
        if (student == null || student.isSuspended()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        // Verify the password
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                student.getPasswordHash()
        );

        // Reject incorrect passwords
        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        // Generate the JWT token
        String token = jwtService.generateToken(
                student.getEmail()
        );

        // Return the token and student information
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