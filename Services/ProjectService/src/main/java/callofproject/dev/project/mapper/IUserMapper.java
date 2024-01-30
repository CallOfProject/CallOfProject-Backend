package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.User;
import callofproject.dev.project.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.project.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
     * @param userDTO The UserDTO to map.
     * @return The mapped User entity.
     */
    @Mapping(target = "roles", source = "roles")
    User toUser(UserDTO userDTO);


    UserKafkaDTO toUserKafkaDTO(User user);

}
