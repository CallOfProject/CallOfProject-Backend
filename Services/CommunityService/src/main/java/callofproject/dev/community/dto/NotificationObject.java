package callofproject.dev.community.dto;

import java.util.UUID;

/**
 * @author Nuri Can ÖZTÜRK
 * NotificationObject
 */
public record NotificationObject(
        UUID projectId,
        UUID userId
)
{
}
