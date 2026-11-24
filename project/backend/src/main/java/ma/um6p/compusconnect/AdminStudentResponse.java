package ma.um6p.compusconnect;

public record AdminStudentResponse(
        Long userId,
        String fullName,
        String email,
        boolean administrator,
        boolean suspended
) {
    // Convert a Student entity into a safe response
    public AdminStudentResponse(Student s) {
        this(
                s.getUserId(),
                s.getFullName(),
                s.getEmail(),
                s.isAdministrator(),
                s.isSuspended()
        );
    }
}