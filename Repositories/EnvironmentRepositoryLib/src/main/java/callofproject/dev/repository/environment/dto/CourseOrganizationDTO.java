package callofproject.dev.repository.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseOrganizationDTO
{
    @JsonProperty("organization_name")
    private String courseOrganizationName;

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
}
