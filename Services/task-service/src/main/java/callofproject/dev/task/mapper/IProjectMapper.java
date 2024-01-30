package callofproject.dev.task.mapper;

import callofproject.dev.data.task.entity.Project;
import callofproject.dev.task.dto.ProjectDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring")
public interface IProjectMapper
{
    ProjectDTO toProjectDTO(Project project);
}
