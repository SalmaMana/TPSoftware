package ma.um6p.compusconnect;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @Column(unique = true, nullable = false)
    private String universityId;

    @Column(nullable = false)
    private String major;

    private Double averageRating;

    // Indicates whether the student has administrator permissions
    private Boolean administrator;

    // Indicates whether the student's account is suspended
    private Boolean suspended;

    protected Student() {
    }

    // Check whether the student is an administrator
    public boolean isAdministrator() {
        return Boolean.TRUE.equals(administrator);
    }

    // Check whether the student's account is suspended
    public boolean isSuspended() {
        return Boolean.TRUE.equals(suspended);
    }

    // Suspend or reactivate a student's account
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}