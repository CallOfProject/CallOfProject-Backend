package callofproject.dev.service.notification.dto;

import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;

import java.util.UUID;

public record NotificationUserResponseDTO(
        UUID toUserId,
        UUID fromUserId,
        String message,
        NotificationType notificationType,
        Object notificationData,
        String notificationLink,
        String notificationImage,
        String notificationTitle,
        String notificationApproveLink,
        String notificationRejectLink,
        NotificationDataType notificationDataType,
        UUID requestId)
{

}
