package ma.um6p.compusconnect;
// Import validation support
import jakarta.validation.Valid;
// Import Spring Web annotations
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Import HTTP response support
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
// @RestController makes this class a REST controller.
// It returns data rather than HTML pages.
@RestController

// All endpoints in this controller start with /api/auth.
@RequestMapping("/api/auth")
public class StudentController {
    // Service containing registration logic
    private final StudentService studentService;
    // Constructor injection:
    // Spring automatically provides the StudentService.
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    // POST /api/auth/register
    // This endpoint is used to register a new student.
    @PostMapping("/register")
    public ResponseEntity<StudentRegistrationResponse> registerStudent(
            // @RequestBody converts incoming JSON into a Java object.
            // @Valid triggers the validation annotations in the DTO.
            @Valid @RequestBody StudentRegistrationRequest request) {
        // Delegate the registration process to the service.
        StudentRegistrationResponse response =
                studentService.registerStudent(request);
        // Return HTTP 201 Created with the response data.
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
