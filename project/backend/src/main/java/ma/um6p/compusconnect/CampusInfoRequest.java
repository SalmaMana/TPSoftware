package ma.um6p.compusconnect;

import jakarta.validation.constraints.*;

public record CampusInfoRequest(

        @NotBlank
        @Size(max = 255)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull
        CampusInfoCategory category,

        @Size(max = 255)
        String location,

        @Size(max = 255)
        String openingHours,

        @Size(max = 255)
        String contactInfo,

        @Size(max = 255)
        String websiteUrl

) {
}