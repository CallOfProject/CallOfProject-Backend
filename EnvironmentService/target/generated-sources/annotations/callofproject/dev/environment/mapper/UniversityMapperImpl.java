package callofproject.dev.environment.mapper;

import callofproject.dev.repository.environment.dto.UniversityDTO;
import callofproject.dev.repository.environment.entity.University;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T11:36:40+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class UniversityMapperImpl implements IUniversityMapper {

    @Override
    public UniversityDTO toUniversityDTO(University university) {
        if ( university == null ) {
            return null;
        }

        String universityName = null;
        long universityId = 0L;

        universityName = university.getUniversityName();
        universityId = university.getUniversityId();

        UniversityDTO universityDTO = new UniversityDTO( universityName, universityId );

        return universityDTO;
    }

    @Override
    public University toUniversity(UniversityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        University university = new University();

        university.setUniversityId( dto.getUniversityId() );
        university.setUniversityName( dto.getUniversityName() );

        return university;
    }
}
