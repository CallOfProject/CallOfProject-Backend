package callofproject.dev.repository.environment.dto;

public class CourseOrganizatorDTO
{
    private String courseOrganizatorName;

    public CourseOrganizatorDTO(String courseOrganizatorName)
    {
        this.courseOrganizatorName = courseOrganizatorName.toUpperCase();
    }

    public String getCourseOrganizatorName()
    {
        return courseOrganizatorName;
    }

}
