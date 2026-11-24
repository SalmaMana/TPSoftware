package ma.um6p.compusconnect;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final StudentRepository studentRepository;

    // Constructor injection.
    public SessionController(
            SessionService sessionService,
            StudentRepository studentRepository) {

        this.sessionService = sessionService;
        this.studentRepository = studentRepository;
    }

    // Retrieve the authenticated student's ID.
    private Long getCurrentStudentId(Authentication authentication) {

        String email = authentication.getName();

        Student student = studentRepository.findByEmail(email);

        if (student == null) {
            throw new ResourceNotFoundException(
                    "Authenticated student not found."
            );
        }

        return student.getUserId();
    }

    // 1. Propose a session.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponse proposeSession(
            @Valid @RequestBody CreateSessionRequest request,
            Authentication authentication) {

        Session session = sessionService.proposeSession(
                getCurrentStudentId(authentication),
                request
        );

        return new SessionResponse(session);
    }

    // 2. Retrieve my sessions.
    @GetMapping
    public List<SessionResponse> getMySessions(
            Authentication authentication) {

        return sessionService
                .getStudentSessions(
                        getCurrentStudentId(authentication)
                )
                .stream()
                .map(SessionResponse::new)
                .toList();
    }

    // 3. Retrieve a specific session.
    @GetMapping("/{sessionId}")
    public SessionResponse getSession(
            @PathVariable Long sessionId,
            Authentication authentication) {

        return new SessionResponse(
                sessionService.getSession(
                        sessionId,
                        getCurrentStudentId(authentication)
                )
        );
    }

    // 4. Accept a session.
    @PatchMapping("/{sessionId}/accept")
    public SessionResponse acceptSession(
            @PathVariable Long sessionId,
            Authentication authentication) {

        return new SessionResponse(
                sessionService.acceptSession(
                        sessionId,
                        getCurrentStudentId(authentication)
                )
        );
    }

    // 5. Reject a session.
    @PatchMapping("/{sessionId}/reject")
    public SessionResponse rejectSession(
            @PathVariable Long sessionId,
            Authentication authentication) {

        return new SessionResponse(
                sessionService.rejectSession(
                        sessionId,
                        getCurrentStudentId(authentication)
                )
        );
    }

    // 6. Cancel a session.
    @PatchMapping("/{sessionId}/cancel")
    public SessionResponse cancelSession(
            @PathVariable Long sessionId,
            Authentication authentication) {

        return new SessionResponse(
                sessionService.cancelSession(
                        sessionId,
                        getCurrentStudentId(authentication)
                )
        );
    }

    // 7. Complete a session.
    @PatchMapping("/{sessionId}/complete")
    public SessionResponse completeSession(
            @PathVariable Long sessionId,
            Authentication authentication) {

        return new SessionResponse(
                sessionService.completeSession(
                        sessionId,
                        getCurrentStudentId(authentication)
                )
        );
    }
}