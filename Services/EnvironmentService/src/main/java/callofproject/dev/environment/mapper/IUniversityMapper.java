package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.UniversitiesDTO;
import callofproject.dev.environment.dto.UniversityDTO;
import callofproject.dev.environment.dto.UniversitySaveDTO;
import callofproject.dev.repository.environment.entity.University;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "UniversityMapperImpl", componentModel = "spring")
public interface IUniversityMapper
{
    UniversityDTO toUniversityDTO(University university);

    University toUniversity(UniversitySaveDTO dto);

    default UniversitiesDTO toUniversitiesDTO(List<UniversityDTO> universityDTOs)
    {
        return new UniversitiesDTO(universityDTOs);
    }
}
