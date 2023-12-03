package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseOrganizationDTO
{
    @JsonProperty("organization_name")
    private String courseOrganizationName;

    @JsonProperty("organization_id")
    private String id;

    public CourseOrganizationDTO()
    {
    }

    public CourseOrganizationDTO(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName.toUpperCase();
    }

    public String getCourseOrganizationName()
    {
        return courseOrganizationName;
    }

    public void setCourseOrganizationName(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
