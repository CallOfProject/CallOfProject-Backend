package callofproject.dev.service.ticket.dto;

import callofproject.dev.data.common.enums.NotificationType;

import java.util.UUID;

public record NotificationDTO(
        UUID toUserId,
        UUID fromUserId,
        String message,
        NotificationType notificationType,
        Object notificationData,
        String notificationLink)
{
}
