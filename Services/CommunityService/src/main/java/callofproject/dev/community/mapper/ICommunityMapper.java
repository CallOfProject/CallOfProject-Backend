package callofproject.dev.community.mapper;

import callofproject.dev.community.dto.CommunityDTO;
import callofproject.dev.community.dto.UserDTO;
import callofproject.dev.data.community.entity.Community;
import org.mapstruct.Mapper;

@Mapper(implementationName = "CommunityMapperImpl", componentModel = "spring")
public interface ICommunityMapper
{
    CommunityDTO toCommunityDTO(Community community, UserDTO projectOwner);
}
