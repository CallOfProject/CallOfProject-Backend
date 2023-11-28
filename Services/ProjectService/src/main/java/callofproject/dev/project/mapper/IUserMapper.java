package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.User;
import callofproject.dev.project.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    User toUser(UserDTO userDTO);
}
