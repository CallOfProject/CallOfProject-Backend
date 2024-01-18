package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data Transfer Object for a course organization.
 */
public class CourseOrganizationSaveDTO
{
    @JsonProperty("organization_name")
    private String courseOrganizationName;


    /**
     * Constructor.
     */
    public CourseOrganizationSaveDTO()
    {
    }


    /**
     * Constructor.
     *
     * @param courseOrganizationName The course organization name.
     */
    public CourseOrganizationSaveDTO(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
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
