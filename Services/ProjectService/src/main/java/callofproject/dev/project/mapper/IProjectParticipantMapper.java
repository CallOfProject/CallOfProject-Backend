package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.project.dto.ProjectParticipantDTO;
import callofproject.dev.project.dto.ProjectsParticipantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Mapper interface for mapping between ProjectParticipant entities and ProjectParticipantDTOs.
 * It provides methods to convert a ProjectParticipant entity to a ProjectParticipantDTO and
 * to wrap a list of ProjectParticipantDTOs into a ProjectsParticipantDTO.
 */
@Mapper(implementationName = "ProjectParticipantMapperImpl", componentModel = "spring", uses = {User.class})
public interface IProjectParticipantMapper
{
    /**
     * Maps a ProjectParticipant entity to a ProjectParticipantDTO.
     *
     * @param projectParticipantDTO The ProjectParticipant entity to map.
     * @return The mapped ProjectParticipantDTO.
     */
    @Mappings({
            @Mapping(target = "userId", source = "user.userId"),
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "fullName", source = "user.fullName"),
            @Mapping(target = "projectId", source = "project.projectId")
    })
    ProjectParticipantDTO toProjectParticipantDTO(ProjectParticipant projectParticipantDTO);

    /**
     * Wraps a list of ProjectParticipantDTOs into a ProjectsParticipantDTO.
     *
     * @param projectParticipantDTOs The list of ProjectParticipantDTOs.
     * @return The ProjectsParticipantDTO containing the list.
     */
    default ProjectsParticipantDTO toProjectsParticipantDTO(List<ProjectParticipantDTO> projectParticipantDTOs)
    {
        return new ProjectsParticipantDTO(projectParticipantDTOs);
    }
}
