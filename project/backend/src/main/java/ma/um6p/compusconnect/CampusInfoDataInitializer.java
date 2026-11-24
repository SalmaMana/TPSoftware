
package ma.um6p.compusconnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Inserts sample campus information when the application starts,
// but only if the database table is empty.
@Component
public class CampusInfoDataInitializer implements CommandLineRunner {

    private final CampusInfoRepository campusInfoRepository;

    // Spring injects the repository automatically.
    public CampusInfoDataInitializer(CampusInfoRepository campusInfoRepository) {
        this.campusInfoRepository = campusInfoRepository;
    }

    // Runs automatically after the application starts.
    @Override
    public void run(String... args) {

        // Avoid inserting sample data if records already exist.
        if (campusInfoRepository.count() > 0) {
            return;
        }

        // Create sample campus information records.
        List<CampusInfo> sampleInformation = List.of(

                new CampusInfo(
                        "Campus Library",
                        "A quiet space where students can study and read.",
                        CampusInfoCategory.STUDY_SPACES,
                        "Main Campus",
                        "08:00 - 22:00",
                        null,
                        null
                ),

                new CampusInfo(
                        "Student Cafeteria",
                        "A campus dining area offering meals and refreshments.",
                        CampusInfoCategory.FOOD,
                        "Main Campus",
                        "12:00 - 15:00",
                        null,
                        null
                ),

                new CampusInfo(
                        "Campus Shuttle",
                        "Information about transportation between campus locations.",
                        CampusInfoCategory.TRANSPORT,
                        "Main Campus",
                        "07:00 - 18:00",
                        null,
                        null
                ),

                new CampusInfo(
                        "Student Administration Office",
                        "Office for general student administrative inquiries.",
                        CampusInfoCategory.ADMINISTRATION,
                        "Main Campus",
                        "09:00 - 16:00",
                        null,
                        null
                ),

                new CampusInfo(
                        "Sports Facility",
                        "A facility dedicated to student sports and physical activities.",
                        CampusInfoCategory.SPORT,
                        "Main Campus",
                        "09:00 - 20:00",
                        null,
                        null
                ),

                new CampusInfo(
                        "Student Health Service",
                        "Information about campus health and support services.",
                        CampusInfoCategory.HEALTH,
                        "Main Campus",
                        null,
                        null,
                        null
                ),

                new CampusInfo(
                        "Campus Event",
                        "A sample event entry used to test the campus information API.",
                        CampusInfoCategory.EVENTS,
                        "Main Campus",
                        null,
                        null,
                        null
                )
        );

        // Save all sample records in the database.
        campusInfoRepository.saveAll(sampleInformation);
        System.out.println("Sample campus information inserted successfully.");
    }
}

