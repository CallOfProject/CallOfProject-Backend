package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.service.interview.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    UserDTO toUserDTO(User user);
}
