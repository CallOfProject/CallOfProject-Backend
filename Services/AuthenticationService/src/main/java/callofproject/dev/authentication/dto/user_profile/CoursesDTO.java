package callofproject.dev.authentication.dto.user_profile;

import java.util.List;

/**
 * Data Transfer Object for a list of courses.
 */
public record CoursesDTO(List<CourseDTO> courses)
{
}
