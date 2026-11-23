
package ma.um6p.compusconnect;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController {

    private final StudentService studentService;

    // Constructor injection
    public StudentProfileController(StudentService studentService) {
        this.studentService = studentService;
    }

    // STEP 5.2: View the logged-in student's profile
    @GetMapping("/me")
    public StudentProfileResponse getMyProfile(
            Authentication authentication) {

        // Get the email of the logged-in student from JWT authentication
        String email = authentication.getName();

        // Retrieve the student's profile
        return studentService.getMyProfile(email);
    }

    // STEP 5.3: Edit the logged-in student's profile
    @PutMapping("/me")
    public StudentProfileResponse updateMyProfile(
            Authentication authentication,
            @RequestBody UpdateStudentProfileRequest request) {

        // Identify the logged-in student
        String email = authentication.getName();

        // Update their profile and return the updated information
        return studentService.updateMyProfile(email, request);
    }
}
