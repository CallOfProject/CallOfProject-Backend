package callofproject.dev.community.service;

import callofproject.dev.community.config.kafka.KafkaProducer;
import callofproject.dev.community.dto.NotificationKafkaDTO;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.community.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.lang.String.format;

@Component
@Lazy
public class ConnectionService implements IConnectionService
{
    private final ConnectionServiceCallback m_connectionServiceCallback;
    private final KafkaProducer m_kafkaProducer;

    @Value("${community.connection.approve-link}")
    private String m_approvalLink;

    @Value("${community.connection.reject-link}")
    private String m_rejectLink;

    public ConnectionService(ConnectionServiceCallback connectionServiceCallback, KafkaProducer kafkaProducer)
    {
        m_connectionServiceCallback = connectionServiceCallback;
        m_kafkaProducer = kafkaProducer;
    }

    @Override
    public ResponseMessage<Object> sendConnectionRequest(UUID userId, UUID friendId)
    {
        var result = doForDataService(() -> m_connectionServiceCallback.sendConnectionRequest(userId, friendId), "ConnectionService::sendConnectionRequest");

        if (result.getStatusCode() == Status.OK)
        {
            var user = m_connectionServiceCallback.findUserByIdIfExist(userId);
            var owner = m_connectionServiceCallback.findUserByIdIfExist(friendId);

            var msg = format("%s sent you a connection request", user.getUsername());
            var approvalLink = format(m_approvalLink, owner.getUserId(), user.getUserId());
            var rejectLink = format(m_rejectLink, owner.getUserId(), user.getUserId());
            sendNotificationToUser(user, owner, msg, approvalLink, rejectLink, "Connection Request");
        }

        return result;
    }

    @Override
    public ResponseMessage<Object> answerConnectionRequest(UUID userId, UUID friendId, boolean answer)
    {
        var result = doForDataService(() -> m_connectionServiceCallback.answerConnectionRequest(userId, friendId, answer), "ConnectionService::answerConnectionRequest");

        if (result.getStatusCode() == Status.OK)
        {
            var user = m_connectionServiceCallback.findUserByIdIfExist(userId);
            var friend = m_connectionServiceCallback.findUserByIdIfExist(friendId);

            var msg = format("%s %s your connection request!", user.getUsername(), answer ? "accepted" : "rejected");
            sendNotificationToUser(user, friend, msg, "", "", "Update Connection Request");
        }

        return result;
    }

    @Override
    public ResponseMessage<Object> removeConnection(UUID userId, UUID friendId)
    {
        return doForDataService(() -> m_connectionServiceCallback.removeConnection(userId, friendId), "ConnectionService::removeConnection");
    }

    @Override
    public MultipleResponseMessagePageable<Object> getConnectionsByUserId(UUID userId)
    {
        return doForDataService(() -> m_connectionServiceCallback.getConnectionsByUserId(userId), "ConnectionService::getConnectionsByUserId");
    }

    @Override
    public ResponseMessage<Object> blockConnection(UUID userId, UUID friendId)
    {
        return doForDataService(() -> m_connectionServiceCallback.blockConnection(userId, friendId), "ConnectionService::blockConnection");
    }

    @Override
    public ResponseMessage<Object> unblockConnection(UUID userId, UUID friendId)
    {
        return doForDataService(() -> m_connectionServiceCallback.unblockConnection(userId, friendId), "ConnectionService::unblockConnection");
    }

    @Override
    public MultipleResponseMessagePageable<Object> getConnectionRequestsByUserId(UUID userId)
    {
        return doForDataService(() -> m_connectionServiceCallback.getConnectionRequestsByUserId(userId), "ConnectionService::getConnectionRequestsByUserId");
    }

    @Override
    public MultipleResponseMessagePageable<Object> getBlockedConnectionsByUserId(UUID userId)
    {
        return doForDataService(() -> m_connectionServiceCallback.getBlockedConnectionsByUserId(userId), "ConnectionService::getBlockedConnectionsByUserId");
    }

    private void sendNotificationToUser(User user, User owner, String message, String approvalLink, String rejectLink, String title)
    {
        var notificationMessage = new NotificationKafkaDTO.Builder()
                .setFromUserId(user.getUserId())
                .setToUserId(owner.getUserId())
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationLink("none")
                .setNotificationImage(null)
                .setNotificationTitle(title)
                .setNotificationDataType(NotificationDataType.REQUEST)
                .setApproveLink(approvalLink)
                .setRejectLink(rejectLink)
                .build();

        // Send notification to project owner
        doForDataService(() -> m_kafkaProducer.sendNotification(notificationMessage), "ProjectService::sendNotificationToProjectOwner");
    }
}