package callofproject.dev.service.filterandsearch.dto;

import callofproject.dev.data.project.entity.enums.EProjectStatus;

import java.util.UUID;

public record   ProjectDTO(
        UUID projectId,
        String projectName,
        String projectImage,
        String projectSummary,
        String projectOwner,
        EProjectStatus projectStatus)
{
}
