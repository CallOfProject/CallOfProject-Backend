package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;

import java.util.UUID;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the interface interview management service.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
public interface IInterviewManagementService
{
    /**
     * Find all interviews by user id.
     *
     * @param userId represents the user id
     * @return The multiple response message
     */
    MultipleResponseMessage<Object> findAllInterviewsByUserId(UUID userId);

    /**
     * Find owner of the interview with the given id.
     *
     * @param interviewId represents the interview id
     * @return The response message
     */
    ResponseMessage<Object> findCodingInterviewOwner(UUID interviewId);

    /**
     * Find owner of the interview with the given id.
     *
     * @param interviewId represents the interview id
     * @return The response message
     */
    ResponseMessage<Object> findTestInterviewOwner(UUID interviewId);
}
