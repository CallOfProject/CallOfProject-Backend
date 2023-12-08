package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.*;
import callofproject.dev.repository.authentication.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(implementationName = "UserProfileMapperImpl", componentModel = "spring",
        uses = {EducationsDTO.class, ExperiencesDTO.class, CoursesDTO.class,
                LinksDTO.class, UserRateDTO.class})
public interface IUserProfileMapper
{

    @Mappings({
            @Mapping(target = "educations", source = "educationsDTO.educations"),
            @Mapping(target = "experiences", source = "experiencesDTO.experiences"),
            @Mapping(target = "courses", source = "coursesDTO.courses"),
            @Mapping(target = "links", source = "linksDTO.links"),
            @Mapping(target = "userRateDTO", source = "userRateDTO")
    })
    UserProfileDTO toUserProfileDTO(UserProfile userProfile,
                                    EducationsDTO educationsDTO, ExperiencesDTO experiencesDTO,
                                    CoursesDTO coursesDTO, LinksDTO linksDTO,
                                    UserRateDTO userRateDTO);
}
