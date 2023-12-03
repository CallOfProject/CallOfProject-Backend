package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseOrganizationDTO
{

    @JsonProperty("course_organization_name")
    private String courseOrganizationName;

    @JsonProperty("id")
    private String id;

    public CourseOrganizationDTO()
    {
    }

    public CourseOrganizationDTO(String courseOrganizationName, String id)
    {
        this.courseOrganizationName = courseOrganizationName;
        this.id = id;
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
