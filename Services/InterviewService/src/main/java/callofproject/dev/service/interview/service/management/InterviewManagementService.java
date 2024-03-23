package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the class interview management service.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service
@Lazy
public class InterviewManagementService implements IInterviewManagementService
{
    private final InterviewManagementCallbackService m_managementCallbackService;

    /**
     * Constructor.
     *
     * @param managementCallbackService represents the management callback service
     */
    public InterviewManagementService(InterviewManagementCallbackService managementCallbackService)
    {
        m_managementCallbackService = managementCallbackService;
    }

    /**
     * Find all interviews by user id.
     *
     * @param userId represents the user id
     * @return the multiple response message
     */
    @Override
    public MultipleResponseMessage<Object> findAllInterviewsByUserId(UUID userId)
    {
        return doForDataService(() -> m_managementCallbackService.findAllInterviewsByUserId(userId), "InterviewManagementService.findAllInterviewsByUserId");
    }

    /**
     * Find all interviews by company id.
     *
     * @param interviewId represents the interview id
     * @return the multiple response message
     */
    @Override
    public ResponseMessage<Object> findCodingInterviewOwner(UUID interviewId)
    {
        return doForDataService(() -> m_managementCallbackService.findCodingInterviewOwner(interviewId), "InterviewManagementService.findCodingInterviewOwner");
    }

    /**
     * Find all interviews by company id.
     *
     * @param interviewId represents the interview id
     * @return the multiple response message
     */
    @Override
    public ResponseMessage<Object> findTestInterviewOwner(UUID interviewId)
    {
        return doForDataService(() -> m_managementCallbackService.findTestInterviewOwner(interviewId), "InterviewManagementService.findTestInterviewOwner");
    }
}
