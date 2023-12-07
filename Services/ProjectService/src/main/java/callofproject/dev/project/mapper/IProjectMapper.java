package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring", uses = {ProjectTag.class,
        Project.class, User.class, ProjectParticipant.class})
public interface IProjectMapper
{
    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle")
    })
    ProjectOverviewDTO toProjectOverviewDTO(Project project, List<ProjectTag> projectTags);

    default ProjectOverviewsDTO toProjectOverviewsDTO(List<ProjectOverviewDTO> projectOverviewDTOs)
    {
        return new ProjectOverviewsDTO(projectOverviewDTOs);
    }

    @Mappings({
            @Mapping(source = "project.projectOwner.username", target = "projectOwnerName"),
            @Mapping(source = "project.projectName", target = "projectTitle"),
            @Mapping(source = "project.sector.sector", target = "sector"),
            @Mapping(source = "project.interviewType.interviewType", target = "interviewType"),
            @Mapping(source = "project.projectAccessType.projectAccessType", target = "projectAccessType"),
            @Mapping(source = "project.degree.degree", target = "degree"),
            @Mapping(source = "project.projectLevel.projectLevel", target = "projectLevel"),
            @Mapping(source = "projectsParticipantDTO.projectParticipants", target = "projectParticipants"),
    })
    ProjectDetailDTO toProjectDetailDTO(Project project, List<ProjectTag> projectTags, ProjectsParticipantDTO projectsParticipantDTO);

    default ProjectsDetailDTO toProjectsDetailDTO(List<ProjectDetailDTO> projectDetailDTOs)
    {
        return new ProjectsDetailDTO(projectDetailDTOs);
    }
}
