package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseOrganizationUpsertDTO
{
    @JsonProperty("experience_id")
    private String id;
    @JsonProperty("organization_name")
    private String courseOrganizationName;

    public CourseOrganizationUpsertDTO(String courseOrganizationName)
    {
        this.courseOrganizationName = courseOrganizationName;
    }

    public CourseOrganizationUpsertDTO(String id, String courseOrganizationName)
    {
        this.id = id;
        this.courseOrganizationName = courseOrganizationName;
    }

    public CourseOrganizationUpsertDTO()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
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
}
