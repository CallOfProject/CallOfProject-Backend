package callofproject.dev.authentication.dto.environments;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CourseOrganizationUpsertDTO
{
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("experience_id")
    private String id;
    @JsonProperty("organization_name")
    private String courseOrganizationName;

    public CourseOrganizationUpsertDTO(UUID userId, String courseOrganizationName)
    {
        this.userId = userId;
        this.courseOrganizationName = courseOrganizationName;
    }

    public CourseOrganizationUpsertDTO(UUID userId, String id, String courseOrganizationName)
    {
        this.userId = userId;
        this.id = id;
        this.courseOrganizationName = courseOrganizationName;
    }

    public CourseOrganizationUpsertDTO()
    {
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
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
