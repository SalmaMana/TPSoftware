
package ma.um6p.compusconnect;

// This class represents the information a student wants to update.
public class UpdateStudentProfileRequest {

    // The student's new full name
    private String fullName;

    // The student's new major
    private String major;

    // Required to read the JSON sent by Postman
    public UpdateStudentProfileRequest() {
    }

    // Returns the new full name
    public String getFullName() {
        return fullName;
    }

    // Returns the new major
    public String getMajor() {
        return major;
    }
}
