package callofproject.dev.community.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;

import java.util.UUID;

public interface IConnectionService
{
    ResponseMessage<Object> sendConnectionRequest(UUID userId, UUID friendId);

    ResponseMessage<Object> answerConnectionRequest(UUID requestId, UUID friendId, boolean answer);

    ResponseMessage<Object> removeConnection(UUID userId, UUID friendId);

    ResponseMessage<Object> blockConnection(UUID userId, UUID friendId);

    ResponseMessage<Object> unblockConnection(UUID userId, UUID friendId);

    MultipleResponseMessagePageable<Object> getConnectionsByUserId(UUID userId);

    MultipleResponseMessagePageable<Object> getConnectionRequestsByUserId(UUID userId);

    MultipleResponseMessagePageable<Object> getBlockedConnectionsByUserId(UUID userId);
}
