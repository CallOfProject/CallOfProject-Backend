package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.EducationDTO;
import callofproject.dev.authentication.dto.user_profile.EducationsDTO;
import callofproject.dev.repository.authentication.entity.Education;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "EducationMapperImpl", componentModel = "spring")
public interface IEducationMapper
{
    @Mapping(target = "educationId", source = "education.education_id")
    EducationDTO toEducationDTO(Education education);

    default EducationsDTO toEducationsDTO(List<EducationDTO> educations)
    {
        return new EducationsDTO(educations);
    }
}
