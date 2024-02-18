package callofproject.dev.community.mapper;


import callofproject.dev.community.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.community.dto.UserDTO;
import callofproject.dev.community.dto.UsersDTO;
import callofproject.dev.data.community.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for mapping between UserDTO and User entity.
 * It provides a method to convert a UserDTO to a User entity.
 */
@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper
{
    /**
     * Maps a UserDTO to a User entity.
     *
     * @param userKafkaDTO The UserDTO to map.
     * @return The mapped User entity.
     */
    @Mapping(target = "roles", source = "roles")
    User toUser(UserKafkaDTO userKafkaDTO);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "username", source = "username")
    UserDTO toUserDTO(User user);

    default UsersDTO toUsersDTO(List<UserDTO> users)
    {
        return new UsersDTO(users);
    }
}
