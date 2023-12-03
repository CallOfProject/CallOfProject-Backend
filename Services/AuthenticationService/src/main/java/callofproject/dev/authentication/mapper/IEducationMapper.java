package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.EducationUpsertDTO;
import callofproject.dev.repository.authentication.entity.Education;
import org.mapstruct.Mapper;

@Mapper(implementationName = "EducationMapperImpl", componentModel = "spring")
public interface IEducationMapper
{
    Education toEducation(EducationUpsertDTO educationUpsertDTO);

}
