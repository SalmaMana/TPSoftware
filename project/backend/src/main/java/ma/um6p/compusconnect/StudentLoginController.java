package ma.um6p.compusconnect;
// Import REST controller functionality
import org.springframework.web.bind.annotation.RestController;
// Import request mapping annotations
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// Import validation
import jakarta.validation.Valid;

// Import HTTP response functionality
import org.springframework.http.ResponseEntity;

// Controller responsible for login requests
@RestController

// Base URL for authentication endpoints
@RequestMapping("/api/auth")
public class StudentLoginController {
    // Service containing the login logic
    private final StudentLoginService studentLoginService;
    // Constructor injection
    public StudentLoginController(StudentLoginService studentLoginService) {
        this.studentLoginService = studentLoginService;
    }
    // Endpoint for student login
    @PostMapping("/login")
    public ResponseEntity<StudentLoginResponse> login(
            @Valid @RequestBody StudentLoginRequest request) {
        // Call the service to authenticate the student
        StudentLoginResponse response = studentLoginService.login(request);
        // Return HTTP 200 OK with the login response
        return ResponseEntity.ok(response);
    }
}
