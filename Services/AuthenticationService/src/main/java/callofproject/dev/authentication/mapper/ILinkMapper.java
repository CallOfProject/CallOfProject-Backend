package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.environments.LinkUpsertDTO;
import callofproject.dev.authentication.dto.user_profile.LinkDTO;
import callofproject.dev.authentication.dto.user_profile.LinksDTO;
import callofproject.dev.repository.authentication.entity.Link;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "LinkMapperImpl", componentModel = "spring")
public interface ILinkMapper
{
    Link toLink(LinkUpsertDTO linkUpsertDTO);

    LinkDTO toLinkDTO(Link link);

    default LinksDTO toLinksDTO(List<LinkDTO> linkDTOs)
    {
        return new LinksDTO(linkDTOs);
    }

}
