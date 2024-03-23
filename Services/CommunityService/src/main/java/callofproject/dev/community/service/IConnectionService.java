package callofproject.dev.community.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;

import java.util.UUID;

/**
 * @author Nuri Can ÖZTÜRK
 * The interface Connection service.
 * This interface is responsible for handling connection requests, answers, removals, blocks, unblocks, and getting connections, connection requests, and blocked connections.
 */
public interface IConnectionService
{
    /**
     * Send connection request response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    ResponseMessage<Object> sendConnectionRequest(UUID userId, UUID friendId);

    /**
     * Answer connection request response message.
     *
     * @param requestId the request id
     * @param friendId  the friend id
     * @param answer    the answer
     * @return the response message
     */
    ResponseMessage<Object> answerConnectionRequest(UUID requestId, UUID friendId, boolean answer);

    /**
     * Remove connection response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    ResponseMessage<Object> removeConnection(UUID userId, UUID friendId);

    /**
     * Block connection response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    ResponseMessage<Object> blockConnection(UUID userId, UUID friendId);

    /**
     * Unblock connection response message.
     *
     * @param userId   the user id
     * @param friendId the friend id
     * @return the response message
     */
    ResponseMessage<Object> unblockConnection(UUID userId, UUID friendId);

    /**
     * Get connections by user id.
     *
     * @param userId the user id
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<Object> getConnectionsByUserId(UUID userId);

    /**
     * Get connection requests by user id.
     *
     * @param userId the user id
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<Object> getConnectionRequestsByUserId(UUID userId);

    /**
     * Get blocked connections by user id.
     *
     * @param userId the user id
     * @return the multiple response message pageable
     */
    MultipleResponseMessagePageable<Object> getBlockedConnectionsByUserId(UUID userId);
}
