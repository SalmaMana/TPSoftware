package ma.um6p.compusconnect;
// Import JPA annotations used to map this class to the database
import jakarta.persistence.*;
// @Entity means that Student is also a database entity.
// It inherits its persistence configuration from User.
@Entity
// This value identifies rows belonging to Student in the shared User table.
// It corresponds to the discriminator column "user_type".
@DiscriminatorValue("STUDENT")
// Student inherits the common attributes from User.
public class Student extends User {
    // The university ID of the student.
    // It must be unique and cannot be null.
    @Column(unique = true, nullable = false)
    private String universityId;
    // The student's academic major or field of study.
    @Column(nullable = false)
    private String major;
    // The student's average rating.
    // We will initialize and update this value later
    // when the review system is implemented.
    private Double averageRating;
    // JPA requires a no-argument constructor
    // to create objects of this entity.
    protected Student() {
    }

    // Returns the student's university ID
    public String getUniversityId() {
        return universityId;
    }
    // Updates the student's university ID
    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }
    // Returns the student's major
    public String getMajor() {
        return major;
    }
    // Updates the student's major
    public void setMajor(String major) {
        this.major = major;
    }
    // Returns the student's average rating
    public Double getAverageRating() {
        return averageRating;
    }
    // Updates the student's average rating
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
