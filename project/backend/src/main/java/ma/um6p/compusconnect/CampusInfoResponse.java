
package ma.um6p.compusconnect;

// DTO representing campus information returned by the API.
public class CampusInfoResponse {
    private Long id;
    private String title;
    private String description;
    private CampusInfoCategory category;
    private String location;
    private String openingHours;
    private String contactInfo;
    private String websiteUrl;
    // Converts a CampusInfo entity into a response DTO.
    public CampusInfoResponse(CampusInfo campusInfo) {
        this.id = campusInfo.getId();
        this.title = campusInfo.getTitle();
        this.description = campusInfo.getDescription();
        this.category = campusInfo.getCategory();
        this.location = campusInfo.getLocation();
        this.openingHours = campusInfo.getOpeningHours();
        this.contactInfo = campusInfo.getContactInfo();
        this.websiteUrl = campusInfo.getWebsiteUrl();
    }
    // Returns the ID.
    public Long getId() {
        return id;
    }

    // Returns the title.
    public String getTitle() {
        return title;
    }

    // Returns the description.
    public String getDescription() {
        return description;
    }

    // Returns the category.
    public CampusInfoCategory getCategory() {
        return category;
    }

    // Returns the location.
    public String getLocation() {
        return location;
    }

    // Returns the opening hours.
    public String getOpeningHours() {
        return openingHours;
    }

    // Returns contact information.
    public String getContactInfo() {
        return contactInfo;
    }

    // Returns the website URL.
    public String getWebsiteUrl() {
        return websiteUrl;
    }
}

