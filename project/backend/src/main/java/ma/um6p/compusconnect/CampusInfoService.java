
package ma.um6p.compusconnect;

import org.springframework.stereotype.Service;
import java.util.List;
// Contains the business logic for campus information.
@Service
public class CampusInfoService {
    // Repository used to access campus information in the database.
    private final CampusInfoRepository campusInfoRepository;
    // Constructor injection: Spring provides the repository automatically.
    public CampusInfoService(CampusInfoRepository campusInfoRepository) {
        this.campusInfoRepository = campusInfoRepository;
    }
    // Retrieves all campus information.
    public List<CampusInfo> getAllCampusInfo() {
        return campusInfoRepository.findAll();
    }

    // Retrieves one campus information item by its ID.
    public CampusInfo getCampusInfoById(Long id) {

        return campusInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Campus information not found with ID: " + id));
    }

    // Retrieves campus information belonging to a specific category.
    public List<CampusInfo> getCampusInfoByCategory(CampusInfoCategory category) {
        return campusInfoRepository.findByCategory(category);
    }

    // Searches campus information by keyword in the title or description.
    public List<CampusInfo> searchCampusInfo(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }

        return campusInfoRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword.trim(), keyword.trim());
    }
}

