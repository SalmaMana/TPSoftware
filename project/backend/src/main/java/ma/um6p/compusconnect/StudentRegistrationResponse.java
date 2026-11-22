
package ma.um6p.compusconnect;
// This class defines the information returned
// to the frontend after successful registration.
public class StudentRegistrationResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String universityId;
    private String major;

    // Constructor used to create the response
    public StudentRegistrationResponse(
            Long userId,
            String fullName,
            String email,
            String universityId,
            String major) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.universityId = universityId;
        this.major = major;
    }

    // Getters allow Jackson to serialize this object into JSON.

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