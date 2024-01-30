package callofproject.dev.task.dto;

import java.util.UUID;

/**
 * NotificationObject
 */
public record NotificationObject(
        UUID projectId,
        UUID userId
)
{
}
