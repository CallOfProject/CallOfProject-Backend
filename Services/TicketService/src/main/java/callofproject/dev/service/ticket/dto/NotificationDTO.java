package callofproject.dev.service.ticket.dto;

import callofproject.dev.data.common.enums.NotificationType;

import java.util.UUID;

/**
 * Represents the notification DTO.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
public record NotificationDTO(
        UUID toUserId,
        UUID fromUserId,
        String message,
        NotificationType notificationType,
        Object notificationData,
        String notificationLink)
{
}
