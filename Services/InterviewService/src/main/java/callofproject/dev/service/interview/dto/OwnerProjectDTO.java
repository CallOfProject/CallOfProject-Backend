package callofproject.dev.service.interview.dto;

import callofproject.dev.data.interview.entity.enums.EProjectStatus;

import java.util.UUID;

public record OwnerProjectDTO(
        UUID projectId,
        String projectName,
        EProjectStatus projectStatus,
        ProjectParticipantsDTO participants)
{
}
