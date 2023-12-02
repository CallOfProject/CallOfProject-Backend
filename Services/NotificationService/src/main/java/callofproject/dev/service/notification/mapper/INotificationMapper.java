package callofproject.dev.service.notification.mapper;

import callofproject.dev.nosql.entity.Notification;
import callofproject.dev.service.notification.dto.NotificationUserResponseDTO;
import org.mapstruct.factory.Mappers;


public interface INotificationMapper
{
    INotificationMapper INSTANCE = Mappers.getMapper(INotificationMapper.class);

    Notification toNotification(NotificationUserResponseDTO notificationDTO);
}
