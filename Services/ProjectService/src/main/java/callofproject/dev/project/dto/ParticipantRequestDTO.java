package callofproject.dev.project.dto;

import java.util.UUID;

public record ParticipantRequestDTO(UUID requestId, boolean isAccepted)
{
}
