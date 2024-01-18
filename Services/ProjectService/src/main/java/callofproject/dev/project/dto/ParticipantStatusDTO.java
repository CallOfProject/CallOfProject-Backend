package callofproject.dev.project.dto;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.User;

/**
 * ParticipantStatusDTO
 */
public record ParticipantStatusDTO(Project project, User user, User owner, boolean isAccepted)
{
}
