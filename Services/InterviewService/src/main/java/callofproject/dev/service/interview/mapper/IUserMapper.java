package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.service.interview.data.entity.User;
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

}
