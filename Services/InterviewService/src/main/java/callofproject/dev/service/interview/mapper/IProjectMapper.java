package callofproject.dev.service.interview.mapper;

import callofproject.dev.service.interview.data.entity.Project;
import callofproject.dev.service.interview.dto.ProjectDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "ProjectMapperImpl", componentModel = "spring")
public interface IProjectMapper
{
    ProjectDTO toProjectDTO(Project project);
}
