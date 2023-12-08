package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.user_profile.UserRateDTO;
import callofproject.dev.repository.authentication.entity.UserRate;
import org.mapstruct.Mapper;

@Mapper(implementationName = "UserRateMapperImpl", componentModel = "spring")
public interface IUserRateMapper
{
    UserRateDTO toUserRateDTO(UserRate userRate);
}
