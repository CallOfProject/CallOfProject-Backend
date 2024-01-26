package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.CourseOrganizationDTO;
import callofproject.dev.environment.dto.CourseOrganizationsDTO;
import callofproject.dev.repository.environment.entity.CourseOrganization;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * ICourseOrganizationMapper
 * - MapStruct interface for mapping CourseOrganization and CourseOrganizationDTO
 */
@Mapper(implementationName = "CourseOrganizationMapperImpl", componentModel = "spring")
public interface ICourseOrganizationMapper
{

    /**
     * toCourseOrganizationDTO
     * - Map CourseOrganization to CourseOrganizationDTO
     *
     * @param organization CourseOrganization
     * @return CourseOrganizationDTO
     */
    CourseOrganizationDTO toCourseOrganizationDTO(CourseOrganization organization);


    /**
     * toCourseOrganization
     * - Map CourseOrganizationDTO to CourseOrganization
     *
     * @param organizationDTO CourseOrganizationDTO
     * @return CourseOrganization
     */
    CourseOrganization toCourseOrganization(CourseOrganizationDTO organizationDTO);


    /**
     * toCourseOrganizationsDTO
     * - Map CourseOrganizationDTOs to CourseOrganizationsDTO
     *
     * @param courseOrganizationsDTOs CourseOrganizationDTOs
     * @return CourseOrganizationsDTO
     */
    default CourseOrganizationsDTO toCourseOrganizationsDTO(List<CourseOrganizationDTO> courseOrganizationsDTOs)
    {
        return new CourseOrganizationsDTO(courseOrganizationsDTOs);
    }
}
