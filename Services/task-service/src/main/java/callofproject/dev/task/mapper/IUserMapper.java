package callofproject.dev.task.mapper;


import callofproject.dev.data.task.entity.User;
import callofproject.dev.task.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.task.dto.UserDTO;
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
     * @param userKafkaDTO The UserDTO to map.
     * @return The mapped User entity.
     */
    @Mapping(target = "roles", source = "roles")
    User toUser(UserKafkaDTO userKafkaDTO);


    /**
     * Maps a User entity to a UserDTO.
     *
     * @param user The User to map.
     * @return The mapped UserDTO.
     */
    UserDTO toUserDTO(User user);
}
