package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


/**
 * Data Transfer Object for a course organization.
 */
public class CourseOrganizationUpsertDTO
{
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("experience_id")
    private String id;
    @JsonProperty("organization_name")
    private String courseOrganizationName;


    /**
     * Constructor for a course organization.
     *
     * @param userId                 The user id.
     * @param courseOrganizationName The course organization name.
     */
    public CourseOrganizationUpsertDTO(UUID userId, String courseOrganizationName)
    {
        this.userId = userId;
        this.courseOrganizationName = courseOrganizationName;
    }


    /**
     * Constructor for a course organization.
     *
     * @param userId                 The user id.
     * @param id                     The course organization id.
     * @param courseOrganizationName The course organization name.
     */
    public CourseOrganizationUpsertDTO(UUID userId, String id, String courseOrganizationName)
    {
        this.userId = userId;
        this.id = id;
        this.courseOrganizationName = courseOrganizationName;
    }


    /**
     * Constructor.
     */
    public CourseOrganizationUpsertDTO()
    {
    }


    /**
     * Gets the user id.
     *
     * @return The user id.
     */
    public UUID getUserId()
    {
        return userId;
    }


    /**
     * Sets the user id.
     *
     * @param userId The user id.
     */
    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }


    /**
     * Gets the course organization id.
     *
     * @return The course organization id.
     */
    public String getId()
    {
        return id;
    }


    /**
     * Sets the course organization id.
     *
     * @param id The course organization id.
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Gets the course organization name.
     *
     * @return The course organization name.
     */
    public String getCourseOrganizationName()
    {
        return courseOrganizationName;
    }


    /**
     * Sets the course organization name.
     *
     * @param courseOrganizationName The course organization name.
     */
    public void setCourseOrganizationName(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }
}
