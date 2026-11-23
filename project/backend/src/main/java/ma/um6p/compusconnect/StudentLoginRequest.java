package ma.um6p.compusconnect;

// Import validation annotations
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// DTO containing the information needed for login
public class StudentLoginRequest {

    // Email must not be empty and must follow email format
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    // Password must not be empty
    @NotBlank(message = "Password is required")
    private String password;

    // Getters allow other classes to access these fields
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters allow Spring to populate the object
    // from the JSON request sent by the client
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
