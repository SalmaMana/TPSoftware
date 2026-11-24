package ma.um6p.compusconnect;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdministrationService {

    private final StudentRepository students;
    private final PostRepository posts;
    private final CampusInfoRepository campus;
    private final ContentReportRepository reports;

    // Constructor injection
    public AdministrationService(
            StudentRepository students,
            PostRepository posts,
            CampusInfoRepository campus,
            ContentReportRepository reports) {

        this.students = students;
        this.posts = posts;
        this.campus = campus;
        this.reports = reports;
    }

    // ================= STUDENT MANAGEMENT =================

    // Find a student by ID
    private Student student(Long id) {
        return students.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));
    }

    // Retrieve all students without exposing passwords
    public List<AdminStudentResponse> listStudents() {

        return students.findAll()
                .stream()
                .map(AdminStudentResponse::new)
                .toList();
    }

    // Suspend or reactivate a student
    public AdminStudentResponse setSuspended(
            Long id, boolean suspended) {

        Student s = student(id);

        // Protect administrator accounts
        if (s.isAdministrator()) {
            throw new IllegalArgumentException(
                    "Cannot suspend or activate an administrator here");
        }

        s.setSuspended(suspended);

        return new AdminStudentResponse(students.save(s));
    }

    // ================= POST MODERATION =================

    // Find a post by ID
    private Post post(Long id) {

        return posts.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));
    }

    // Retrieve all posts
    public List<PostResponse> listPosts() {

        return posts.findAll()
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    // Delete an inappropriate post
    public void deletePost(Long id) {

        posts.delete(post(id));
    }

    // ================= CAMPUS INFORMATION =================

    // Find a campus information item
    private CampusInfo item(Long id) {

        return campus.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Campus information not found"));
    }

    // Create or update campus information
    public CampusInfoResponse saveCampus(
            Long id, CampusInfoRequest r) {

        // If ID is null, create a new item.
        // Otherwise, retrieve the existing item.
        CampusInfo c = id == null ?
                new CampusInfo() : item(id);

        c.setTitle(r.title());
        c.setDescription(r.description());
        c.setCategory(r.category());
        c.setLocation(r.location());
        c.setOpeningHours(r.openingHours());
        c.setContactInfo(r.contactInfo());
        c.setWebsiteUrl(r.websiteUrl());

        return new CampusInfoResponse(campus.save(c));
    }

    // Delete campus information
    public void deleteCampus(Long id) {

        campus.delete(item(id));
    }

    // ================= CONTENT REPORTING =================

    // Submit a report against a post or student
    public ContentReport report(
            String email, CreateReportRequest r) {

        // Identify the reporter from authentication
        Student reporter = students.findByEmail(email);

        if (reporter == null) {
            throw new ResourceNotFoundException(
                    "Reporter not found");
        }

        // Exactly one target must be provided
        if ((r.postId() == null) == (r.studentId() == null)) {

            throw new IllegalArgumentException(
                    "Provide exactly one report target");
        }

        // Retrieve the reported post if applicable
        Post p = r.postId() == null ?
                null : post(r.postId());

        // Identify the reported student
        Long target = p == null ?
                student(r.studentId()).getUserId() :
                p.getAuthor().getUserId();

        // Prevent students from reporting themselves
        if (target.equals(reporter.getUserId())) {

            throw new IllegalArgumentException(
                    "Cannot report yourself");
        }

        // Create and save the report
        ContentReport report = new ContentReport(
                reporter.getUserId(),
                p,
                target,
                r.reason().trim()
        );

        return reports.save(report);
    }

    // Retrieve all submitted reports
    public List<ContentReport> listReports() {

        return reports.findAll();
    }

    // Mark a report as resolved
    public ContentReport resolve(Long id, String email) {

        ContentReport r = reports.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found"));

        // Prevent resolving the same report twice
        if (r.isResolved()) {

            throw new IllegalArgumentException(
                    "Report already resolved");
        }

        // Identify the administrator
        Student admin = students.findByEmail(email);

        // Record who resolved the report
        r.resolve(admin.getUserId());

        return reports.save(r);
    }
}