package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.data.entity.Project;
import callofproject.dev.service.interview.data.entity.ProjectParticipant;
import callofproject.dev.service.interview.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring", uses = {UserDTO.class, ProjectParticipantDTO.class,
        ProjectParticipant.class, IUserMapper.class})
public interface IProjectMapper
{
    ProjectDTO toProjectDTO(Project project);

    @Mapping(source = "participantsDTO", target = "participants")
    OwnerProjectDTO toOwnerProjectDTO(Project project, ProjectParticipantsDTO participantsDTO);

    @Mapping(source = "user", target = "user")
    ProjectParticipantDTO toProjectParticipantDTO(ProjectParticipant projectParticipant);

    default ProjectParticipantsDTO toProjectsParticipantDTO(List<ProjectParticipantDTO> participantsDTO)
    {
        return new ProjectParticipantsDTO(participantsDTO);
    }

    default OwnerProjectsDTO toOwnerProjectsDTO(List<OwnerProjectDTO> ownerProjects)
    {
        return new OwnerProjectsDTO(ownerProjects);
    }
}
