package callofproject.dev.authentication.mapper;

import callofproject.dev.library.constant.dto.usermanagement.UserSignUpRequestDTO;
import callofproject.dev.repository.authentication.entity.User;
import org.mapstruct.Mapper;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    User toUser(UserSignUpRequestDTO dto);
}
