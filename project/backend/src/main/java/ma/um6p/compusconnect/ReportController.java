package ma.um6p.compusconnect;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final AdministrationService service;

    // Constructor injection
    public ReportController(AdministrationService service) {

        this.service = service;
    }

    // Allow an authenticated student to submit a report
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContentReport report(
            @Valid @RequestBody CreateReportRequest r,
            Authentication auth) {

        // Retrieve the reporter's email from authentication
        return service.report(auth.getName(), r);
    }
}