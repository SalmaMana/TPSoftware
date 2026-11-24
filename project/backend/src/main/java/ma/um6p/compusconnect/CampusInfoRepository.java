
package ma.um6p.compusconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
// Provides database operations for CampusInfo entities.
public interface CampusInfoRepository extends JpaRepository<CampusInfo, Long> {
    // Retrieves campus information belonging to a specific category.
    List<CampusInfo> findByCategory(CampusInfoCategory category);
    // Retrieves campus information whose title or description contains a keyword.
    List<CampusInfo> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword, String descriptionKeyword);
}

