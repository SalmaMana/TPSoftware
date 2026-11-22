package ma.um6p.compusconnect;
// Import JPA annotations used to map this Java class to the database
import jakarta.persistence.*;
// @Entity tells JPA that this class represents an entity
// that should be stored and managed in the database.
@Entity
// We use SINGLE_TABLE inheritance because Student and Administrator
// will share the same database table with User.
//
// Instead of creating a separate table for each child class,
// all users will be stored in one table.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// This column will distinguish between the different types of users.
// For example: STUDENT or ADMIN.
@DiscriminatorColumn(name = "user_type")
public class User {
    // @Id indicates that this field is the primary key of the table.
    // Each user must have a unique identifier.
    @Id
    // The database automatically generates the ID when a new user is inserted.
    // IDENTITY uses the database's auto-increment mechanism.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    // Stores the full name of the user.
    // This field is shared by Student and Administrator.
    private String fullName;
    // Stores the user's email address.
    //
    // unique = true prevents two users from having the same email.
    // nullable = false means this column cannot contain SQL NULL.
    @Column(unique = true, nullable = false)
    private String email;
    // Stores the hashed password, NOT the original password.
    //
    // nullable = false means every user must have a password hash.
    // We will hash passwords using Spring Security before saving them.
    @Column(nullable = false)
    private String passwordHash;

    // Returns the user's ID
    public Long getUserId() {
        return userId;
    }
    // Returns the user's full name
    public String getFullName() {
        return fullName;
    }
    // Updates the user's full name
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Returns the user's email
    public String getEmail() {
        return email;
    }
    // Updates the user's email
    public void setEmail(String email) {
        this.email = email;
    }
    // Returns the stored password hash
    public String getPasswordHash() {
        return passwordHash;
    }
    // Updates the stored password hash
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
