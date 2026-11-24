package ma.um6p.compusconnect;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdministrationService service;

    // Constructor injection
    public AdminController(AdministrationService service) {

        this.service = service;
    }

    // ================= STUDENT MANAGEMENT =================

    // Retrieve all students
    @GetMapping("/students")
    public List<AdminStudentResponse> students() {

        return service.listStudents();
    }

    // Suspend a student
    @PatchMapping("/students/{id}/suspend")
    public AdminStudentResponse suspend(@PathVariable Long id) {

        return service.setSuspended(id, true);
    }

    // Reactivate a student
    @PatchMapping("/students/{id}/activate")
    public AdminStudentResponse activate(@PathVariable Long id) {

        return service.setSuspended(id, false);
    }

    // ================= POST MODERATION =================

    // Retrieve all posts
    @GetMapping("/posts")
    public List<PostResponse> posts() {

        return service.listPosts();
    }

    // Delete an inappropriate post
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {

        service.deletePost(id);
    }

    // ================= CAMPUS INFORMATION =================

    // Create campus information
    @PostMapping("/campus-info")
    @ResponseStatus(HttpStatus.CREATED)
    public CampusInfoResponse createCampus(
            @Valid @RequestBody CampusInfoRequest r) {

        return service.saveCampus(null, r);
    }

    // Update existing campus information
    @PutMapping("/campus-info/{id}")
    public CampusInfoResponse updateCampus(
            @PathVariable Long id,
            @Valid @RequestBody CampusInfoRequest r) {

        return service.saveCampus(id, r);
    }

    // Delete campus information
    @DeleteMapping("/campus-info/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampus(@PathVariable Long id) {

        service.deleteCampus(id);
    }

    // ================= REPORT MANAGEMENT =================

    // Retrieve all submitted reports
    @GetMapping("/reports")
    public List<ContentReport> reports() {

        return service.listReports();
    }

    // Mark a report as resolved
    @PatchMapping("/reports/{id}/resolve")
    public ContentReport resolve(
            @PathVariable Long id,
            Authentication auth) {

        return service.resolve(id, auth.getName());
    }
}