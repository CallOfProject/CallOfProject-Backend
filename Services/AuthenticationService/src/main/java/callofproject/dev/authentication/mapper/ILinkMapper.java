package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.environments.LinkCreateDTO;
import callofproject.dev.authentication.dto.user_profile.LinkDTO;
import callofproject.dev.authentication.dto.user_profile.LinksDTO;
import callofproject.dev.repository.authentication.entity.Link;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper class for mapping Link entities to LinkDTOs.
 */
@Mapper(implementationName = "LinkMapperImpl", componentModel = "spring")
public interface ILinkMapper
{
    /**
     * Maps a LinkUpsertDTO to a Link entity.
     *
     * @param linkCreateDTO The LinkUpsertDTO to be mapped.
     * @return A Link entity representing the mapped LinkUpsertDTO.
     */
    Link toLink(LinkCreateDTO linkCreateDTO);

    /**
     * Maps a Link entity to a LinkDTO.
     *
     * @param link The Link entity to be mapped.
     * @return A LinkDTO representing the mapped Link entity.
     */
    @Mapping(target = "linkId", source = "link.link_id")
    LinkDTO toLinkDTO(Link link);

    /**
     * Maps a list of Link entities to a LinksDTO.
     *
     * @param linkDTOs The list of Link entities to be mapped.
     * @return A LinksDTO representing the mapped list of Link entities.
     */
    default LinksDTO toLinksDTO(List<LinkDTO> linkDTOs)
    {
        return new LinksDTO(linkDTOs);
    }
}
