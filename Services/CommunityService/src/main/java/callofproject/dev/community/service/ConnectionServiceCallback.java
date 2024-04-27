package callofproject.dev.community.service;

import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Nuri Can ÖZTÜRK
 * The type Connection service callback.
 * This class is responsible for handling connection requests, answers, removals, blocks, unblocks, and getting connections, connection requests, and blocked connections.
 */
@Component
@Lazy
public class ConnectionServiceCallback
{
    private final CommunityServiceHelper m_communityServiceHelper;
    private final IUserMapper m_userMapper;

    /**
     * Instantiates a new Connection service callback.
     *
     * @param communityServiceHelper the community service helper
     * @param userMapper             the user mapper
     */
    public ConnectionServiceCallback(CommunityServiceHelper communityServiceHelper, IUserMapper userMapper)
    {
        m_communityServiceHelper = communityServiceHelper;
        m_userMapper = userMapper;
    }

    /**
     * Send connection request response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    public ResponseMessage<Object> sendConnectionRequest(UUID userId, UUID friendId)
    {
        var user = findUserByIdIfExist(userId);
        var friend = findUserByIdIfExist(friendId);

        friend.addConnectionRequest(user);
        m_communityServiceHelper.upsertUser(friend);

        return new ResponseMessage<>("Connection request sent successfully", Status.OK, true);
    }

    /**
     * Answer connection request response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @param answer   the answer
     * @return the response message
     */
    public ResponseMessage<Object> answerConnectionRequest(UUID userId, UUID friendId, boolean answer)
    {
        var user = findUserByIdIfExist(friendId);
        var friend = findUserByIdIfExist(userId);

        // Remove connection request
        user.getConnectionRequests().removeIf(u -> u.getUserId().equals(friend.getUserId()));
        friend.getConnectionRequests().removeIf(u -> u.getUserId().equals(user.getUserId()));


        // Add connection if answer is true
        if (answer)
        {
            user.addConnection(friend);
            friend.addConnection(user);
        }

        // Update users
        m_communityServiceHelper.upsertUser(user);
        m_communityServiceHelper.upsertUser(friend);

        return new ResponseMessage<>("Connection request answered is: " + answer, Status.OK, answer);
    }

    /**
     * Remove connection response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    public ResponseMessage<Object> removeConnection(UUID userId, UUID friendId)
    {
        var user = findUserByIdIfExist(friendId);
        var friend = findUserByIdIfExist(userId);

        // Remove connection request
        user.getConnections().removeIf(u -> u.getUserId().equals(friend.getUserId()));
        friend.getConnections().removeIf(u -> u.getUserId().equals(user.getUserId()));

        m_communityServiceHelper.upsertUser(user);
        m_communityServiceHelper.upsertUser(friend);

        return new ResponseMessage<>("Connection removed successfully", Status.OK, true);
    }

    /**
     * Get connections by user id.
     *
     * @param userId the user id
     * @return the connections by user id
     */
    public MultipleResponseMessagePageable<Object> getConnectionsByUserId(UUID userId)
    {
        var user = findUserByIdIfExist(userId);
        var connections = m_userMapper.toUsersDTO(user.getConnections().stream().map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, connections.users().size(), "Connections retrieved successfully", connections);
    }

    /**
     * Block connection response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    public ResponseMessage<Object> blockConnection(UUID userId, UUID friendId)
    {
        var user = findUserByIdIfExist(friendId);
        var friend = findUserByIdIfExist(userId);

        user.getConnections().removeIf(u -> u.getUserId().equals(friend.getUserId()));
        friend.getConnections().removeIf(u -> u.getUserId().equals(user.getUserId()));

        user.addBlockedConnection(friend);
        friend.addBlockedConnection(user);

        m_communityServiceHelper.upsertUser(user);
        m_communityServiceHelper.upsertUser(friend);

        return new ResponseMessage<>("Connection blocked successfully", Status.OK, true);
    }

    /**
     * Unblock connection response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    public ResponseMessage<Object> unblockConnection(UUID userId, UUID friendId)
    {
        var user = findUserByIdIfExist(friendId);
        var friend = findUserByIdIfExist(userId);

        user.getBlockedConnections().removeIf(u -> u.getUserId().equals(friend.getUserId()));

        user.addConnection(friend);
        friend.addConnection(user);

        m_communityServiceHelper.upsertUser(user);

        return new ResponseMessage<>("Connection unblocked successfully", Status.OK, true);
    }

    /**
     * Get connection requests by user id.
     *
     * @param userId the user id
     * @return the connection requests by user id
     */
    public MultipleResponseMessagePageable<Object> getConnectionRequestsByUserId(UUID userId)
    {
        var user = findUserByIdIfExist(userId);
        var connections = m_userMapper.toUsersDTO(user.getConnectionRequests().stream().map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, connections.users().size(), "Connection Requests retrieved successfully", connections);
    }

    /**
     * Get blocked connections by user id.
     *
     * @param userId the user id
     * @return the blocked connections by user id
     */
    public MultipleResponseMessagePageable<Object> getBlockedConnectionsByUserId(UUID userId)
    {
        var user = findUserByIdIfExist(userId);
        var connections = m_userMapper.toUsersDTO(user.getBlockedConnections().stream().map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, connections.users().size(), "Blocked Connections retrieved successfully", connections);
    }

    /**
     * Find user by id if exist.
     *
     * @param userId the user id
     * @return the user
     */
    public User findUserByIdIfExist(UUID userId)
    {
        return m_communityServiceHelper.findUserById(userId).orElseThrow(() -> new DataServiceException("User not found"));
    }
}