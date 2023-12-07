package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.environments.CourseOrganizationUpsertDTO;
import callofproject.dev.repository.authentication.entity.CourseOrganization;
import org.mapstruct.Mapper;

@Mapper(implementationName = "CourseOrganizationMapperImpl", componentModel = "spring")
public interface ICourseOrganizationMapper
{
    CourseOrganization toCourseOrganization(CourseOrganizationUpsertDTO organizationUpsertDTO);
}
