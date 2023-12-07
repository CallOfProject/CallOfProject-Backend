package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.environments.ExperienceUpsertDTO;
import callofproject.dev.repository.authentication.entity.Experience;
import org.mapstruct.Mapper;

@Mapper(implementationName = "ExperienceMapperImpl", componentModel = "spring")
public interface IExperienceMapper
{
    Experience toExperience(ExperienceUpsertDTO experienceUpsertDTO);

}
