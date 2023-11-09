package callofproject.dev.environment.mapper;

import callofproject.dev.repository.environment.dto.CourseOrganizatorDTO;
import callofproject.dev.repository.environment.entity.CourseOrganizator;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T11:36:40+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class CourseOrganizatorMapperImpl implements ICourseOrganizatorMapper {

    @Override
    public CourseOrganizatorDTO toCourseOrganizatorDTO(CourseOrganizator courseOrganizator) {
        if ( courseOrganizator == null ) {
            return null;
        }

        String courseOrganizatorName = null;

        CourseOrganizatorDTO courseOrganizatorDTO = new CourseOrganizatorDTO( courseOrganizatorName );

        return courseOrganizatorDTO;
    }

    @Override
    public CourseOrganizator toCourseOrganizator(CourseOrganizatorDTO courseOrganizatorDTO) {
        if ( courseOrganizatorDTO == null ) {
            return null;
        }

        CourseOrganizator courseOrganizator = new CourseOrganizator();

        return courseOrganizator;
    }
}
