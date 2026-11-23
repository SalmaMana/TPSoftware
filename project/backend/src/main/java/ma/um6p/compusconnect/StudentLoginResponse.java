package ma.um6p.compusconnect;

// DTO containing the information returned after successful login
public class StudentLoginResponse {

    // JWT authentication token
    private String token;

    // Student information
    private Long userId;
    private String fullName;
    private String email;
    private String universityId;
    private String major;

    // Constructor to initialize the response
    public StudentLoginResponse(
            String token,
            Long userId,
            String fullName,
            String email,
            String universityId,
            String major) {

        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.universityId = universityId;
        this.major = major;
    }

    // Getters allow Spring to serialize the object into JSON
    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUniversityId() {
        return universityId;
    }

    public String getMajor() {
        return major;
    }
}
