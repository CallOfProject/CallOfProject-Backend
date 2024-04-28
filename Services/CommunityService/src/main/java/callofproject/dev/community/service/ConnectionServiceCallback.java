package callofproject.dev.community.service;

import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.entity.BlockConnection;
import callofproject.dev.data.community.entity.ConnectionRequest;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.data.community.entity.UserConnection;
import callofproject.dev.library.exception.service.DataServiceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.util.stream.StreamUtil.toStream;

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

        var connectionRequest = new ConnectionRequest(user, friend);
        user.addConnectionRequest(connectionRequest);
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
        var req1 = user.getConnectionRequests().stream().filter(u -> u.getRequestee().getUserId().equals(friend.getUserId())).findFirst();
        req1.ifPresent(user.getConnectionRequests()::remove);

        //remove connection request from db
        req1.ifPresent(m_communityServiceHelper::deleteConnectionRequest);


        if (answer)
        {
            var connection1 = new UserConnection(user, friend);
            var connection2 = new UserConnection(friend, user);

            user.addUserConnection(connection1);
            friend.addUserConnection(connection2);
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
        var user = findUserByIdIfExist(userId);
        var friend = findUserByIdIfExist(friendId);

        var con1 = user.getConnections().stream().filter(u -> u.getConnectedUser().getUserId().equals(friend.getUserId())).findFirst();
        /*con1.get().setConnectedUser(null);
        con1.get().setMainUser(null);*/
        var con2 = friend.getConnections().stream().filter(u -> u.getConnectedUser().getUserId().equals(user.getUserId())).findFirst();
        /*con2.get().setConnectedUser(null);
        con2.get().setMainUser(null);*/

        con1.ifPresent(user.getConnections()::remove);
        con2.ifPresent(friend.getConnections()::remove);

        con1.ifPresent(m_communityServiceHelper::deleteUserConnection);
        con2.ifPresent(m_communityServiceHelper::deleteUserConnection);

       /* m_communityServiceHelper.deleteUserConnection(con1.get());
        m_communityServiceHelper.deleteUserConnection(con2.get());

        m_communityServiceHelper.upsertUserConnection(con1.get());
        m_communityServiceHelper.upsertUserConnection(con2.get());*/



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
        var userConnections = toStream(m_communityServiceHelper.findUserConnectionsByUserId(user.getUserId()));
        var connections = m_userMapper.toUsersDTO(userConnections.map(UserConnection::getConnectedUser).map(m_userMapper::toUserDTO).toList());
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


        var block1 = new BlockConnection(user, friend);
        var block2 = new BlockConnection(friend, user);

        user.addBlockedConnection(block1);
        friend.addBlockedConnection(block2);

        m_communityServiceHelper.upsertUser(user);
        m_communityServiceHelper.upsertUser(friend);
        removeConnection(userId, friendId);
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

        var blocked1 = user.getBlockedConnections().stream().filter(u -> u.getBlockedUser().getUserId().equals(friend.getUserId())).findFirst();
        var blocked2 = friend.getBlockedConnections().stream().filter(u -> u.getBlockedUser().getUserId().equals(user.getUserId())).findFirst();

        blocked1.ifPresent(user.getBlockedConnections()::remove);
        blocked2.ifPresent(friend.getBlockedConnections()::remove);

        blocked1.ifPresent(m_communityServiceHelper::deleteBlockConnection);
        blocked2.ifPresent(m_communityServiceHelper::deleteBlockConnection);

        var connection1 = new UserConnection(user, friend);
        var connection2 = new UserConnection(friend, user);

        user.addUserConnection(connection1);
        friend.addUserConnection(connection2);

        m_communityServiceHelper.upsertUser(user);
        m_communityServiceHelper.upsertUser(friend);

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
        var connectionRequests = m_userMapper.toUsersDTO(user.getConnectionRequests()
                .stream().map(ConnectionRequest::getRequestee)
                .map(m_userMapper::toUserDTO).toList());

        return new MultipleResponseMessagePageable<>(1, 1, connectionRequests.users().size(), "Connection Requests retrieved successfully", connectionRequests);
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
        var blockedConnections = m_userMapper.toUsersDTO(user.getBlockedConnections().stream().map(BlockConnection::getBlockedUser).map(m_userMapper::toUserDTO).toList());
        return new MultipleResponseMessagePageable<>(1, 1, blockedConnections.users().size(), "Blocked Connections retrieved successfully", blockedConnections);
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