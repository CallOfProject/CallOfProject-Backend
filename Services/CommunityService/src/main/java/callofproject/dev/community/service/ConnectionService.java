package callofproject.dev.community.service;

import callofproject.dev.community.config.kafka.KafkaProducer;
import callofproject.dev.community.dto.NotificationKafkaDTO;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.nosql.dal.NotificationServiceHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.data.common.enums.NotificationDataType.CONNECTION_REQUEST;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.lang.String.format;

/**
 * @author Nuri Can ÖZTÜRK
 * The type Connection service.
 * This class is responsible for handling connection requests, answers, removals, blocks, unblocks, and getting connections, connection requests, and blocked connections.
 */
@Component
@Lazy
public class ConnectionService implements IConnectionService
{
    private final ConnectionServiceCallback m_connectionServiceCallback;
    private final NotificationServiceHelper m_notificationServiceHelper;
    private final KafkaProducer m_kafkaProducer;

    @Value("${community.connection.approve-link}")
    private String m_approvalLink;

    @Value("${community.connection.reject-link}")
    private String m_rejectLink;

    /**
     * Constructor
     *
     * @param connectionServiceCallback the connection service callback
     * @param kafkaProducer             the kafka producer
     */
    public ConnectionService(ConnectionServiceCallback connectionServiceCallback, NotificationServiceHelper notificationServiceHelper, KafkaProducer kafkaProducer)
    {
        m_connectionServiceCallback = connectionServiceCallback;
        m_notificationServiceHelper = notificationServiceHelper;
        m_kafkaProducer = kafkaProducer;
    }

    /**
     * Send connection request to a user
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
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
            sendNotificationToUser(user, owner, msg, approvalLink, rejectLink, "Connection Request", false);
        }

        return result;
    }

    /**
     * Answer connection request
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @param answer   the answer
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> answerConnectionRequest(UUID userId, UUID friendId, boolean answer, String notificationId)
    {
        var result = doForDataService(() -> m_connectionServiceCallback.answerConnectionRequest(userId, friendId, answer, notificationId),
                "ConnectionService::answerConnectionRequest");

        if (result.getStatusCode() == Status.OK)
        {
            var user = m_connectionServiceCallback.findUserByIdIfExist(userId);
            var friend = m_connectionServiceCallback.findUserByIdIfExist(friendId);

            var msg = format("%s %s your connection request!", user.getUsername(), answer ? "accepted" : "rejected");
            if (!notificationId.isEmpty())
                m_notificationServiceHelper.deleteNotificationById(notificationId);
            else
            {
                var notification = m_notificationServiceHelper.findByUserIdAndNotificationDataType(friend.getUserId(), user.getUserId(),
                        callofproject.dev.nosql.enums.NotificationDataType.CONNECTION_REQUEST);
                notification.ifPresent(value -> m_notificationServiceHelper.deleteNotificationById(value.getId()));
            }
            sendNotificationToUser(user, friend, msg, "", "", "Update Connection Request", true);
        }

        return result;
    }

    /**
     * Remove connection
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> removeConnection(UUID userId, UUID friendId)
    {
        return doForDataService(() -> m_connectionServiceCallback.removeConnection(userId, friendId), "ConnectionService::removeConnection");
    }

    /**
     * Get user by id if exist
     *
     * @param userId the user id
     * @return the user
     */
    @Override
    public MultipleResponseMessagePageable<Object> getConnectionsByUserId(UUID userId)
    {
        return doForDataService(() -> m_connectionServiceCallback.getConnectionsByUserId(userId), "ConnectionService::getConnectionsByUserId");
    }

    /**
     * Block connection
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> blockConnection(UUID userId, UUID friendId)
    {
        return doForDataService(() -> m_connectionServiceCallback.blockConnection(userId, friendId), "ConnectionService::blockConnection");
    }

    /**
     * Unblock connection
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> unblockConnection(UUID userId, UUID friendId)
    {
        return doForDataService(() -> m_connectionServiceCallback.unblockConnection(userId, friendId), "ConnectionService::unblockConnection");
    }

    /**
     * Get connection requests by user id
     *
     * @param userId the user id
     * @return the response message
     */
    @Override
    public MultipleResponseMessagePageable<Object> getConnectionRequestsByUserId(UUID userId)
    {
        return doForDataService(() -> m_connectionServiceCallback.getConnectionRequestsByUserId(userId), "ConnectionService::getConnectionRequestsByUserId");
    }

    /**
     * Get blocked connections by user id
     *
     * @param userId the user id
     * @return the response message
     */
    @Override
    public MultipleResponseMessagePageable<Object> getBlockedConnectionsByUserId(UUID userId)
    {
        return doForDataService(() -> m_connectionServiceCallback.getBlockedConnectionsByUserId(userId), "ConnectionService::getBlockedConnectionsByUserId");
    }

    private void sendNotificationToUser(User user, User owner, String message, String approvalLink, String rejectLink, String title, boolean isAnswer)
    {
        var notificationMessage = new NotificationKafkaDTO.Builder()
                .setFromUserId(user.getUserId())
                .setToUserId(owner.getUserId())
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationLink("none")
                .setNotificationImage(null)
                .setNotificationTitle(title)
                .setNotificationDataType(isAnswer ? NotificationDataType.COMMUNITY : CONNECTION_REQUEST)
                .setApproveLink(approvalLink)
                .setRejectLink(rejectLink)
                .build();

        doForDataService(() -> m_kafkaProducer.sendNotification(notificationMessage), "ProjectService::sendNotificationToProjectOwner");
    }
}