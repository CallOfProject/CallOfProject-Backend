package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.CourseUpsertDTO;
import callofproject.dev.repository.authentication.entity.Course;
import org.mapstruct.Mapper;

@Mapper(implementationName = "CourseMapperImpl", componentModel = "spring")
public interface ICourseMapper
{
    Course toCourse(CourseUpsertDTO courseUpsertDTO);
}
