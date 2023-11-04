package callofproject.dev.repository.environment.dto;

public record CourseOrganizatorDTO(String courseOrganizatorName)
{
    public CourseOrganizatorDTO(String courseOrganizatorName)
    {
        this.courseOrganizatorName = courseOrganizatorName.toUpperCase();
    }

}
