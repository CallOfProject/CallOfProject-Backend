package callofproject.dev.environment.mapper;

import callofproject.dev.repository.environment.dto.CourseOrganizationDTO;
import callofproject.dev.repository.environment.dto.CourseOrganizationsDTO;
import callofproject.dev.repository.environment.entity.CourseOrganization;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CourseOrganizationMapperImpl", componentModel = "spring")
public interface ICourseOrganizationMapper
{
    CourseOrganizationDTO toCourseOrganizationDTO(CourseOrganization organization);

    CourseOrganization toCourseOrganization(CourseOrganizationDTO organizationDTO);

    default CourseOrganizationsDTO toCourseOrganizationsDTO(List<CourseOrganizationDTO> courseOrganizationsDTOs)
    {
        return new CourseOrganizationsDTO(courseOrganizationsDTOs);
    }
}
