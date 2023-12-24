package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.User;
import callofproject.dev.project.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    @Mapping(target = "roles", source = "roles")
    User toUser(UserDTO userDTO);

}
