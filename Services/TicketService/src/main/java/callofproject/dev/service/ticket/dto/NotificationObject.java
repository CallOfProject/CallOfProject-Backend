package callofproject.dev.service.ticket.dto;

import java.util.UUID;

/**
 * Notification object
 * <p>
 * This class contains the notification object
 */
public record NotificationObject(
        UUID projectId,
        UUID userId
)
{
}
