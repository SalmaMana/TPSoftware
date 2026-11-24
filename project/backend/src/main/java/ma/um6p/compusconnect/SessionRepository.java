package ma.um6p.compusconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository
        extends JpaRepository<Session, Long> {

    // Retrieve all sessions involving a student.
    List<Session> findByProposer_UserIdOrReceiver_UserId(
            Long proposerId,
            Long receiverId
    );
}