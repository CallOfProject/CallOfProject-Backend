package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ProjectsParticipantDTO;
import callofproject.dev.project.dto.detail.ProjectDetailDTO;
import callofproject.dev.project.dto.detail.ProjectsDetailDTO;
import callofproject.dev.project.dto.discovery.ProjectDiscoveryDTO;
import callofproject.dev.project.dto.discovery.ProjectsDiscoveryDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewsDTO;
import callofproject.dev.project.dto.owner.ProjectOwnerViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring", uses = {ProjectTag.class, Project.class, User.class, ProjectParticipant.class})
public interface IProjectMapper
{
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
    })
    ProjectOverviewDTO toProjectOverviewDTO(Project project, List<ProjectTag> projectTags);

    default ProjectOverviewsDTO toProjectOverviewsDTO(List<ProjectOverviewDTO> projectOverviewDTOs)
    {
        return new ProjectOverviewsDTO(projectOverviewDTOs);
    }

    //------------------------------------------------------------------------------------------------------------------
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
            @Mapping(source = "projectsParticipantDTO.projectParticipants", target = "projectParticipants"),
    })
    ProjectDetailDTO toProjectDetailDTO(Project project, List<ProjectTag> projectTags, ProjectsParticipantDTO projectsParticipantDTO);

    default ProjectsDetailDTO toProjectsDetailDTO(List<ProjectDetailDTO> projectDetailDTOs)
    {
        return new ProjectsDetailDTO(projectDetailDTOs);
    }

    //------------------------------------------------------------------------------------------------------------------
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle")
    })
    ProjectDiscoveryDTO toProjectDiscoveryDTO(Project project);

    default ProjectsDiscoveryDTO toProjectsDiscoveryDTO(List<ProjectDiscoveryDTO> projectDiscoveryDTOs)
    {
        return new ProjectsDiscoveryDTO(projectDiscoveryDTOs);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
            @Mapping(source = "projectsParticipantDTO.projectParticipants", target = "projectParticipants"),
    })
    ProjectOwnerViewDTO toProjectOwnerViewDTO(Project project, List<ProjectTag> projectTags, ProjectsParticipantDTO projectsParticipantDTO);
}