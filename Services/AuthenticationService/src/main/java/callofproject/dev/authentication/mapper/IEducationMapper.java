package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.EducationDTO;
import callofproject.dev.authentication.dto.user_profile.EducationsDTO;
import callofproject.dev.repository.authentication.entity.Education;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper class for mapping Education entities to EducationDTOs.
 */
@Mapper(implementationName = "EducationMapperImpl", componentModel = "spring")
public interface IEducationMapper
{
    /**
     * Maps an Education entity to an EducationDTO.
     *
     * @param education The Education entity to be mapped.
     * @return An EducationDTO representing the mapped Education entity.
     */
    @Mapping(target = "educationId", source = "education.education_id")
    @Mapping(target = "isContinue", source = "education.isContinue")
    EducationDTO toEducationDTO(Education education);

    /**
     * Maps a list of Education entities to an EducationsDTO.
     *
     * @param educations The list of Education entities to be mapped.
     * @return An EducationsDTO representing the mapped list of Education entities.
     */
    default EducationsDTO toEducationsDTO(List<EducationDTO> educations)
    {
        return new EducationsDTO(educations);
    }
}
