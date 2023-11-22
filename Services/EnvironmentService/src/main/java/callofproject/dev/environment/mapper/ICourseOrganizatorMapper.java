package callofproject.dev.environment.mapper;

import callofproject.dev.repository.environment.dto.CompanyDTO;
import callofproject.dev.repository.environment.dto.CourseOrganizatorDTO;
import callofproject.dev.repository.environment.dto.CourseOrganizatorsDTO;
import callofproject.dev.repository.environment.entity.CourseOrganizator;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CourseOrganizatorMapperImpl", componentModel = "spring")
public interface ICourseOrganizatorMapper
{
    CourseOrganizatorDTO toCourseOrganizatorDTO(CourseOrganizator courseOrganizator);

    CourseOrganizator toCourseOrganizator(CourseOrganizatorDTO courseOrganizatorDTO);

    default CourseOrganizatorsDTO toCourseOrganizatorsDTO(List<CourseOrganizatorDTO> courseOrganizatorDTOs)
    {
        return new CourseOrganizatorsDTO(courseOrganizatorDTOs);
    }
}
