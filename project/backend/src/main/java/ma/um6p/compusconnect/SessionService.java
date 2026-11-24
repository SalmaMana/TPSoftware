package ma.um6p.compusconnect;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final StudentRepository studentRepository;

    // Constructor injection.
    public SessionService(
            SessionRepository sessionRepository,
            StudentRepository studentRepository) {

        this.sessionRepository = sessionRepository;
        this.studentRepository = studentRepository;
    }

    // 1. Propose a new session.
    @Transactional
    public Session proposeSession(
            Long proposerId,
            CreateSessionRequest request) {

        Long receiverId = request.getReceiverId();

        // Prevent self-session proposals.
        if (proposerId.equals(receiverId)) {
            throw new IllegalArgumentException(
                    "You cannot propose a session to yourself."
            );
        }

        // Verify that both students exist.
        Student proposer = studentRepository.findById(proposerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proposer not found."
                ));

        Student receiver = studentRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receiver not found."
                ));

        // Verify that the proposed date is in the future.
        if (!request.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Session date must be in the future."
            );
        }

        // Create the session.
        Session session = new Session();

        session.setProposer(proposer);
        session.setReceiver(receiver);
        session.setScheduledAt(request.getScheduledAt());
        session.setDetails(request.getDetails().trim());
        session.setStatus(SessionStatus.PENDING);

        return sessionRepository.save(session);
    }

    // 2. Retrieve all sessions of a student.
    @Transactional(readOnly = true)
    public List<Session> getStudentSessions(Long studentId) {

        return sessionRepository
                .findByProposer_UserIdOrReceiver_UserId(
                        studentId, studentId
                );
    }

    // 3. Retrieve a session and verify access.
    @Transactional(readOnly = true)
    public Session getSession(Long sessionId, Long studentId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Session not found."
                ));

        boolean isProposer = studentId.equals(
                session.getProposer().getUserId()
        );

        boolean isReceiver = studentId.equals(
                session.getReceiver().getUserId()
        );

        if (!isProposer && !isReceiver) {
            throw new UnauthorizedActionException(
                    "You cannot access this session."
            );
        }

        return session;
    }

    // 4. Accept a session proposal.
    @Transactional
    public Session acceptSession(Long sessionId, Long studentId) {

        Session session = getSession(sessionId, studentId);

        // Only the receiver can accept.
        if (!studentId.equals(session.getReceiver().getUserId())) {
            throw new UnauthorizedActionException(
                    "Only the receiver can accept the session."
            );
        }

        // Only pending sessions can be accepted.
        if (session.getStatus() != SessionStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending sessions can be accepted."
            );
        }

        if (!session.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "The proposed session date has already passed."
            );
        }

        session.setStatus(SessionStatus.ACCEPTED);

        return sessionRepository.save(session);
    }

    // 5. Reject a session proposal.
    @Transactional
    public Session rejectSession(Long sessionId, Long studentId) {

        Session session = getSession(sessionId, studentId);

        // Only the receiver can reject.
        if (!studentId.equals(session.getReceiver().getUserId())) {
            throw new UnauthorizedActionException(
                    "Only the receiver can reject the session."
            );
        }

        if (session.getStatus() != SessionStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending sessions can be rejected."
            );
        }

        session.setStatus(SessionStatus.REJECTED);

        return sessionRepository.save(session);
    }

    // 6. Cancel a session.
    @Transactional
    public Session cancelSession(Long sessionId, Long studentId) {

        Session session = getSession(sessionId, studentId);

        // Only pending or accepted sessions can be cancelled.
        if (session.getStatus() != SessionStatus.PENDING
                && session.getStatus() != SessionStatus.ACCEPTED) {

            throw new IllegalArgumentException(
                    "This session cannot be cancelled."
            );
        }

        session.setStatus(SessionStatus.CANCELLED);

        return sessionRepository.save(session);
    }

    // 7. Mark a session as completed.
    @Transactional
    public Session completeSession(Long sessionId, Long studentId) {

        Session session = getSession(sessionId, studentId);

        // Only accepted sessions can be completed.
        if (session.getStatus() != SessionStatus.ACCEPTED) {
            throw new IllegalArgumentException(
                    "Only accepted sessions can be completed."
            );
        }

        // Prevent completion before the scheduled time.
        if (session.getScheduledAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "The session has not started yet."
            );
        }

        session.setStatus(SessionStatus.COMPLETED);

        return sessionRepository.save(session);
    }
}