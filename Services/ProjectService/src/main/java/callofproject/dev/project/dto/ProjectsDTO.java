package callofproject.dev.project.dto;

import callofproject.dev.project.dto.overview.ProjectOverviewDTO;

import java.util.List;

public record ProjectsDTO(List<ProjectOverviewDTO> projects)
{
}
