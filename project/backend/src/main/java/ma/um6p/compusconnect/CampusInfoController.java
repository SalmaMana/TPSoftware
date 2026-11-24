
package ma.um6p.compusconnect;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// Exposes REST endpoints for campus information.
@RestController
@RequestMapping("/api/campus-info")
public class CampusInfoController {
    // Service responsible for the business logic.
    private final CampusInfoService campusInfoService;
    // Constructor injection.
    public CampusInfoController(CampusInfoService campusInfoService) {
        this.campusInfoService = campusInfoService;
    }
    // GET /api/campus-info
    // Retrieves all campus information.
    @GetMapping
    public List<CampusInfoResponse> getAllCampusInfo() {
        return campusInfoService.getAllCampusInfo()
                .stream()
                .map(CampusInfoResponse::new)
                .toList();
    }

    // GET /api/campus-info/{id}
    // Retrieves one item by its ID.
    @GetMapping("/{id}")
    public CampusInfoResponse getCampusInfoById(@PathVariable Long id) {

        return new CampusInfoResponse(
                campusInfoService.getCampusInfoById(id));
    }

    // GET /api/campus-info/category/{category}
    // Retrieves items belonging to a specific category.
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getCampusInfoByCategory(
            @PathVariable String category) {

        try {
            CampusInfoCategory categoryEnum = CampusInfoCategory.valueOf(category.toUpperCase());

            List<CampusInfoResponse> response = campusInfoService
                    .getCampusInfoByCategory(categoryEnum)
                    .stream()
                    .map(CampusInfoResponse::new)
                    .toList();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid campus information category: " + category);
        }
    }

    // GET /api/campus-info/search?keyword=library
    // Searches campus information by keyword.
    @GetMapping("/search")
    public List<CampusInfoResponse> searchCampusInfo(
            @RequestParam String keyword) {

        return campusInfoService.searchCampusInfo(keyword)
                .stream()
                .map(CampusInfoResponse::new)
                .toList();
    }
}

