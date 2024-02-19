package callofproject.dev.service.interview.dto;

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
