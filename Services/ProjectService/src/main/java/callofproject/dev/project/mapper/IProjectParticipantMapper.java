package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.project.dto.ProjectParticipantDTO;
import callofproject.dev.project.dto.ProjectsParticipantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(implementationName = "ProjectParticipantMapperImpl", componentModel = "spring", uses = {User.class})
public interface IProjectParticipantMapper
{
    @Mappings({
            @Mapping(target = "userId", source = "user.userId"),
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "fullName", source = "user.fullName"),
            @Mapping(target = "projectId", source = "project.projectId")
    })
    ProjectParticipantDTO toProjectParticipantDTO(ProjectParticipant projectParticipantDTO);

    default ProjectsParticipantDTO toProjectsParticipantDTO(List<ProjectParticipantDTO> projectParticipantDTOs)
    {
        return new ProjectsParticipantDTO(projectParticipantDTOs);
    }
}
