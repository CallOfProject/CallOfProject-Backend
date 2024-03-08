package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.*;
import callofproject.dev.repository.authentication.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper class for mapping UserProfile entities to UserProfileDTOs.
 */
@Mapper(implementationName = "UserProfileMapperImpl", componentModel = "spring",
        uses = {EducationsDTO.class, ExperiencesDTO.class, CoursesDTO.class,
                LinksDTO.class, UserRateDTO.class})
public interface IUserProfileMapper
{

    /**
     * Maps a UserProfile entity to a UserProfileDTO.
     *
     * @param userProfile The UserProfile entity to be mapped.
     * @return A UserProfileDTO representing the mapped UserProfile entity.
     */
    @Mappings({
            @Mapping(target = "educations", source = "educationsDTO.educations"),
            @Mapping(target = "experiences", source = "experiencesDTO.experiences"),
            @Mapping(target = "courses", source = "coursesDTO.courses"),
            @Mapping(target = "links", source = "linksDTO.links")
    })
    UserProfileDTO toUserProfileDTO(UserProfile userProfile,
                                    EducationsDTO educationsDTO, ExperiencesDTO experiencesDTO,
                                    CoursesDTO coursesDTO, LinksDTO linksDTO);
}
