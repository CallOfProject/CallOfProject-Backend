package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ProjectAdminDTO;
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

/**
 * Mapper interface for mapping between Project entities and various project-related DTOs.
 * It provides mappings to convert between Project entity and DTOs like ProjectOverviewDTO,
 * ProjectDetailDTO, ProjectDiscoveryDTO, and ProjectOwnerViewDTO.
 * Additionally, it converts lists of these DTOs into their corresponding container DTOs.
 */
@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring", uses = {ProjectTag.class, Project.class, User.class, ProjectParticipant.class})
public interface IProjectMapper
{
    /**
     * Maps a Project entity and a list of ProjectTag entities to a ProjectOverviewDTO.
     *
     * @param project     The Project entity to map.
     * @param projectTags The list of ProjectTag entities.
     * @return The mapped ProjectOverviewDTO.
     */
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
    })
    ProjectOverviewDTO toProjectOverviewDTO(Project project, List<ProjectTag> projectTags);

    /**
     * Wraps a list of ProjectOverviewDTOs into a ProjectOverviewsDTO.
     *
     * @param projectOverviewDTOs The list of ProjectOverviewDTOs.
     * @return The ProjectOverviewsDTO containing the list.
     */
    default ProjectOverviewsDTO toProjectOverviewsDTO(List<ProjectOverviewDTO> projectOverviewDTOs)
    {
        return new ProjectOverviewsDTO(projectOverviewDTOs);
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Maps from Project entity and related data to ProjectDetailDTO.
     *
     * @param project                The Project entity to be mapped.
     * @param projectTags            The list of associated ProjectTag entities.
     * @param projectsParticipantDTO The ProjectsParticipantDTO containing project participants' information.
     * @return The mapped ProjectDetailDTO.
     */
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
            @Mapping(source = "projectsParticipantDTO.projectParticipants", target = "projectParticipants"),
    })
    ProjectDetailDTO toProjectDetailDTO(Project project, List<ProjectTag> projectTags, ProjectsParticipantDTO projectsParticipantDTO);

    /**
     * Wraps a list of ProjectDetailDTOs into a ProjectsDetailDTO.
     *
     * @param projectDetailDTOs The list of ProjectDetailDTOs to wrap.
     * @return The ProjectsDetailDTO containing the list.
     */
    default ProjectsDetailDTO toProjectsDetailDTO(List<ProjectDetailDTO> projectDetailDTOs)
    {
        return new ProjectsDetailDTO(projectDetailDTOs);
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Maps from Project entity and image path to ProjectDiscoveryDTO.
     *
     * @param project The Project entity to be mapped.
     * @param image   The image path associated with the project.
     * @return The mapped ProjectDiscoveryDTO.
     */
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
            @Mapping(source = "image", target = "projectImagePath")
    })
    ProjectDiscoveryDTO toProjectDiscoveryDTO(Project project, String image);

    /**
     * Maps from Project entity to ProjectDiscoveryDTO. Used when no image path is available.
     *
     * @param project The Project entity to be mapped.
     * @return The mapped ProjectDiscoveryDTO.
     */
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
    })
    ProjectDiscoveryDTO toProjectDiscoveryDTO(Project project);

    /**
     * Wraps a list of ProjectDiscoveryDTOs into a ProjectsDiscoveryDTO.
     *
     * @param projectDiscoveryDTOs The list of ProjectDiscoveryDTOs to wrap.
     * @return The ProjectsDiscoveryDTO containing the list.
     */
    default ProjectsDiscoveryDTO toProjectsDiscoveryDTO(List<ProjectDiscoveryDTO> projectDiscoveryDTOs)
    {
        return new ProjectsDiscoveryDTO(projectDiscoveryDTOs);
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Maps from Project entity, list of ProjectTags, and ProjectsParticipantDTO to ProjectOwnerViewDTO.
     *
     * @param project                The Project entity to be mapped.
     * @param projectTags            The list of associated ProjectTag entities.
     * @param projectsParticipantDTO The ProjectsParticipantDTO containing project participants' information.
     * @return The mapped ProjectOwnerViewDTO.
     */
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
            @Mapping(source = "projectsParticipantDTO.projectParticipants", target = "projectParticipants"),
    })
    ProjectOwnerViewDTO toProjectOwnerViewDTO(Project project, List<ProjectTag> projectTags, ProjectsParticipantDTO projectsParticipantDTO);


    /**
     * Maps from Project entity to ProjectAdminDTO.
     *
     * @param project The Project entity to be mapped.
     * @return The mapped ProjectAdminDTO.
     */
    @Mapping(source = "project.projectOwner.username", target = "projectOwnerUsername")
    @Mapping(target = "currentParticipants", expression = "java(project.getProjectParticipants().size())")
    @Mapping(target = "maxParticipants", source = "project.maxParticipant")
    ProjectAdminDTO toProjectAdminDTO(Project project);
}