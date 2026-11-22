package ma.um6p.compusconnect;
// Import validation annotations
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// This class represents the information received
// from the frontend during student registration.
public class StudentRegistrationRequest {
    // The student's full name cannot be empty.
    @NotBlank(message = "Full name is required")
    private String fullName;
    // The email cannot be empty and must have a valid email format.
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    // The password must be provided and contain at least 8 characters.
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;
    // The university ID is required.
    @NotBlank(message = "University ID is required")
    private String universityId;
    // The student's major is required.
    @NotBlank(message = "Major is required")
    private String major;
    // Getter for fullName
    public String getFullName() {
        return fullName;
    }
    // Getter for email
    public String getEmail() {
        return email;
    }
    // Getter for password
    public String getPassword() {
        return password;
    }
    // Getter for universityId
    public String getUniversityId() {
        return universityId;
    }
    // Getter for major
    public String getMajor() {
        return major;
    }

    // Setters used to populate the request object
    // from the JSON received by the application.

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}