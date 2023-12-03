package callofproject.dev.authentication.mapper;

import callofproject.dev.authentication.dto.LinkUpsertDTO;
import callofproject.dev.repository.authentication.entity.Link;
import org.mapstruct.Mapper;

@Mapper(implementationName = "LinkMapperImpl", componentModel = "spring")
public interface ILinkMapper
{
    Link toLink(LinkUpsertDTO linkUpsertDTO);

}
