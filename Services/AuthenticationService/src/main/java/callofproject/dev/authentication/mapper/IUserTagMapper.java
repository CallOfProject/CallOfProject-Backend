package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.UserTagDTO;
import callofproject.dev.authentication.dto.UserTagsDTO;
import callofproject.dev.repository.authentication.entity.UserTag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "UserTagMapperImpl", componentModel = "spring")
public interface IUserTagMapper
{
    UserTagDTO toUserTagDTO(UserTag userTag);

    default UserTagsDTO toUserTagsDTO(List<UserTagDTO> userTags)
    {
        return new UserTagsDTO(userTags);
    }
}
