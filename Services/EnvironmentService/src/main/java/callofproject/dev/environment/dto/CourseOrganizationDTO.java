package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CourseOrganizationDTO
 */
public class CourseOrganizationDTO
{
    @JsonProperty("organization_name")
    private String courseOrganizationName;

    @JsonProperty("organization_id")
    private String id;


    /**
     * Constructor
     */
    public CourseOrganizationDTO()
    {
    }


    /**
     * Constructor
     *
     * @param courseOrganizationName course organization name
     */
    public CourseOrganizationDTO(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName.toUpperCase();
    }


    /**
     * Getter
     *
     * @return course organization name
     */
    public String getCourseOrganizationName()
    {
        return courseOrganizationName;
    }


    /**
     * Setter
     *
     * @param courseOrganizationName course organization name
     */
    public void setCourseOrganizationName(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }


    /**
     * Getter
     *
     * @return course organization id
     */
    public String getId()
    {
        return id;
    }


    /**
     * Setter
     *
     * @param id course organization id
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
