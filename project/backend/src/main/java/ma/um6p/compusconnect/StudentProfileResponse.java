package ma.um6p.compusconnect;

public class StudentProfileResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String universityId;
    private String major;
    private Double averageRating;

    // Constructor
    public StudentProfileResponse(Long userId, String fullName,
                                  String email, String universityId,
                                  String major, Double averageRating) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.universityId = universityId;
        this.major = major;
        this.averageRating = averageRating;
    }

    // Getters
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

    public Double getAverageRating() {
        return averageRating;
    }
}