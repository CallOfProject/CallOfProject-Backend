package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.ExperienceDTO;
import callofproject.dev.authentication.dto.user_profile.ExperiencesDTO;
import callofproject.dev.repository.authentication.entity.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "ExperienceMapperImpl", componentModel = "spring")
public interface IExperienceMapper
{
    @Mapping(target = "position", source = "experience.jobDefinition")
    @Mapping(target = "experienceId", source = "experience.experience_id")
    ExperienceDTO toExperienceDTO(Experience experience);

    default ExperiencesDTO toExperiencesDTO(List<ExperienceDTO> experiences)
    {
        return new ExperiencesDTO(experiences);
    }
}
