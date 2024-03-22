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

@Component
@Lazy
public class ConnectionServiceCallback
{
    private final CommunityServiceHelper m_communityServiceHelper;
    private final IUserMapper m_userMapper;

    public ConnectionServiceCallback(CommunityServiceHelper communityServiceHelper, IUserMapper userMapper)
    {
        m_communityServiceHelper = communityServiceHelper;
        m_userMapper = userMapper;
    }

    public ResponseMessage<Object> sendConnectionRequest(UUID userId, UUID friendId)
    {
        var user = findUserByIdIfExist(userId);
        var friend = findUserByIdIfExist(friendId);

        friend.addConnectionRequest(user);
        m_communityServiceHelper.upsertUser(friend);

        return new ResponseMessage<>("Connection request sent successfully", Status.OK, true);
    }

    public ResponseMessage<Object> answerConnectionRequest(UUID userId, UUID friendId, boolean answer)
    {
        var user = findUserByIdIfExist(friendId);
        var friend = findUserByIdIfExist(userId);

        // Remove connection request
        user.getConnectionRequests().removeIf(u -> u.getUserId().equals(friend.getUserId()));

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

    public MultipleResponseMessagePageable<Object> getConnectionsByUserId(UUID userId)
    {
        var user = findUserByIdIfExist(userId);
        var connections = m_userMapper.toUsersDTO(user.getConnections().stream().map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, connections.users().size(), "Connections retrieved successfully", connections);
    }

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

    public MultipleResponseMessagePageable<Object> getConnectionRequestsByUserId(UUID userId)
    {
        var user = findUserByIdIfExist(userId);
        var connections = m_userMapper.toUsersDTO(user.getConnectionRequests().stream().map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, connections.users().size(), "Connection Requests retrieved successfully", connections);
    }

    public MultipleResponseMessagePageable<Object> getBlockedConnectionsByUserId(UUID userId)
    {
        var user = findUserByIdIfExist(userId);
        var connections = m_userMapper.toUsersDTO(user.getBlockedConnections().stream().map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, connections.users().size(), "Blocked Connections retrieved successfully", connections);
    }

    public User findUserByIdIfExist(UUID userId)
    {
        var user = m_communityServiceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User not found");

        return user.get();
    }
}