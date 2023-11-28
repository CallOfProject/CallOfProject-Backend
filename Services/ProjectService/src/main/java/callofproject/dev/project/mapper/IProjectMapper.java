package callofproject.dev.project.mapper;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.project.dto.ProjectOverviewDTO;
import callofproject.dev.project.dto.ProjectOverviewsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring", uses = {ProjectTag.class, Project.class, User.class})
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
}
