package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.CourseDTO;
import callofproject.dev.environment.dto.CoursesDTO;
import callofproject.dev.repository.environment.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CourseMapperImpl", componentModel = "spring")
public interface ICourseMapper
{
    CourseDTO toCourseDTO(Course course);

    Course toCourse(CourseDTO courseDTO);

    default CoursesDTO toCoursesDTO(List<CourseDTO> courseDTOList)
    {
        return new CoursesDTO(courseDTOList);
    }
}
