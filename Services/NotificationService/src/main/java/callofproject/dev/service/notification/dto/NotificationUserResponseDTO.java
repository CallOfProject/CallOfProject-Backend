package callofproject.dev.service.notification.dto;

import callofproject.dev.nosql.enums.NotificationType;

import java.util.UUID;

public record NotificationUserResponseDTO(
        UUID toUserId,
        UUID fromUserId,
        String message,
        NotificationType notificationType,
        Object notificationData,
        String notificationLink
)
{

}
