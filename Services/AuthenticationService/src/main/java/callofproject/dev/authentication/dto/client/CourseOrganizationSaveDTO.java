package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseOrganizationSaveDTO
{
    @JsonProperty("organization_name")
    private String courseOrganizationName;

    public CourseOrganizationSaveDTO()
    {
    }

    public CourseOrganizationSaveDTO(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public String getCourseOrganizationName()
    {
        return courseOrganizationName;
    }

    public void setCourseOrganizationName(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }
}
