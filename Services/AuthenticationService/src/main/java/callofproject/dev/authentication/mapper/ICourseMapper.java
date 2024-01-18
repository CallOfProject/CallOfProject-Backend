package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.CourseDTO;
import callofproject.dev.authentication.dto.user_profile.CoursesDTO;
import callofproject.dev.repository.authentication.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper class for mapping Course entities to CourseDTOs.
 */
@Mapper(implementationName = "CourseMapperImpl", componentModel = "spring")
public interface ICourseMapper
{
    /**
     * Maps a Course entity to a CourseDTO.
     *
     * @param course The Course entity to be mapped.
     * @return A CourseDTO representing the mapped Course entity.
     */
    @Mapping(target = "organization", source = "course.courseOrganization.courseOrganizationName")
    @Mapping(target = "courseId", source = "course.course_id")
    CourseDTO toCourseDTO(Course course);

    /**
     * Maps a list of Course entities to a CoursesDTO.
     *
     * @param courses The list of Course entities to be mapped.
     * @return A CoursesDTO representing the mapped list of Course entities.
     */
    default CoursesDTO toCoursesDTO(List<CourseDTO> courses)
    {
        return new CoursesDTO(courses);
    }
}
