package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data Transfer Object for a course organization.
 */
public class CourseOrganizationDTO
{

    @JsonProperty("course_organization_name")
    private String courseOrganizationName;

    @JsonProperty("id")
    private String id;


    /**
     * Constructor.
     */
    public CourseOrganizationDTO()
    {
    }


    /**
     * Constructor.
     *
     * @param courseOrganizationName The course organization name.
     */
    public CourseOrganizationDTO(String courseOrganizationName, String id)
    {
        this.courseOrganizationName = courseOrganizationName;
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
}
