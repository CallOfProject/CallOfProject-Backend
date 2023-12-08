package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.CourseDTO;
import callofproject.dev.authentication.dto.user_profile.CoursesDTO;
import callofproject.dev.repository.authentication.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "CourseMapperImpl", componentModel = "spring")
public interface ICourseMapper
{
    @Mapping(target = "organization", source = "course.courseOrganization.courseOrganizationName")
    @Mapping(target = "courseId", source = "course.course_id")
    CourseDTO toCourseDTO(Course course);

    default CoursesDTO toCoursesDTO(List<CourseDTO> courses)
    {
        return new CoursesDTO(courses);
    }
}
