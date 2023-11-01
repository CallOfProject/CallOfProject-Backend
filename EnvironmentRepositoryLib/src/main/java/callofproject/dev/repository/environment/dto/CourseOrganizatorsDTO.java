package callofproject.dev.repository.environment.dto;

import java.util.List;

public class CourseOrganizatorsDTO
{
    private List<CourseOrganizatorDTO> course_organizators;

    public CourseOrganizatorsDTO(List<CourseOrganizatorDTO> courseOrganizators)
    {
        course_organizators = courseOrganizators;
    }

    public List<CourseOrganizatorDTO> getCourseOrganizators()
    {
        return course_organizators;
    }
}
