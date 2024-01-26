package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.CourseDTO;
import callofproject.dev.environment.dto.CoursesDTO;
import callofproject.dev.repository.environment.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * ICourseMapper
 * - MapStruct interface for mapping Course and CourseDTO
 */
@Mapper(implementationName = "CourseMapperImpl", componentModel = "spring")
public interface ICourseMapper
{

    /**
     * toCourseDTO
     * - Map Course to CourseDTO
     *
     * @param course Course
     * @return CourseDTO
     */
    CourseDTO toCourseDTO(Course course);


    /**
     * toCourse
     * - Map CourseDTO to Course
     *
     * @param courseDTO CourseDTO
     * @return Course
     */
    Course toCourse(CourseDTO courseDTO);


    /**
     * toCoursesDTO
     * - Map CourseDTOs to CoursesDTO
     *
     * @param courseDTOList CourseDTOs
     * @return CoursesDTO
     */
    default CoursesDTO toCoursesDTO(List<CourseDTO> courseDTOList)
    {
        return new CoursesDTO(courseDTOList);
    }
}
