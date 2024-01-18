package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.ExperienceDTO;
import callofproject.dev.authentication.dto.user_profile.ExperiencesDTO;
import callofproject.dev.repository.authentication.entity.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper class for mapping Experience entities to ExperienceDTOs.
 */
@Mapper(implementationName = "ExperienceMapperImpl", componentModel = "spring")
public interface IExperienceMapper
{
    /**
     * Maps an Experience entity to an ExperienceDTO.
     *
     * @param experience The Experience entity to be mapped.
     * @return An ExperienceDTO representing the mapped Experience entity.
     */
    @Mapping(target = "position", source = "experience.jobDefinition")
    @Mapping(target = "experienceId", source = "experience.experience_id")
    ExperienceDTO toExperienceDTO(Experience experience);

    /**
     * Maps a list of Experience entities to an ExperiencesDTO.
     *
     * @param experiences The list of Experience entities to be mapped.
     * @return An ExperiencesDTO representing the mapped list of Experience entities.
     */
    default ExperiencesDTO toExperiencesDTO(List<ExperienceDTO> experiences)
    {
        return new ExperiencesDTO(experiences);
    }
}
