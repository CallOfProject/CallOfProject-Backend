package callofproject.dev.environment.mapper;

import callofproject.dev.environment.dto.UniversitiesDTO;
import callofproject.dev.environment.dto.UniversityDTO;
import callofproject.dev.environment.dto.UniversitySaveDTO;
import callofproject.dev.repository.environment.entity.University;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * IUniversityMapper
 * - MapStruct interface for mapping University and UniversityDTO
 */
@Mapper(implementationName = "UniversityMapperImpl", componentModel = "spring")
public interface IUniversityMapper
{

    /**
     * toUniversityDTO
     * - Map University to UniversityDTO
     *
     * @param university University
     * @return UniversityDTO
     */
    UniversityDTO toUniversityDTO(University university);


    /**
     * toUniversity
     * - Map UniversityDTO to University
     *
     * @param dto UniversityDTO
     * @return University
     */
    University toUniversity(UniversitySaveDTO dto);


    /**
     * toUniversitiesDTO
     * - Map UniversityDTOs to UniversitiesDTO
     *
     * @param universityDTOs UniversityDTOs
     * @return UniversitiesDTO
     */
    default UniversitiesDTO toUniversitiesDTO(List<UniversityDTO> universityDTOs)
    {
        return new UniversitiesDTO(universityDTOs);
    }
}
