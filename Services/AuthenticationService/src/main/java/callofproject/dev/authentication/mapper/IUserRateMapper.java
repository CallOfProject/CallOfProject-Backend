package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.UserRateDTO;
import callofproject.dev.repository.authentication.entity.UserRate;
import org.mapstruct.Mapper;

/**
 * Mapper class for mapping UserRate entities to UserRateDTOs.
 */
@Mapper(implementationName = "UserRateMapperImpl", componentModel = "spring")
public interface IUserRateMapper
{
    /**
     * Maps a UserRate entity to a UserRateDTO.
     *
     * @param userRate The UserRate entity to be mapped.
     * @return A UserRateDTO representing the mapped UserRate entity.
     */
    UserRateDTO toUserRateDTO(UserRate userRate);
}
