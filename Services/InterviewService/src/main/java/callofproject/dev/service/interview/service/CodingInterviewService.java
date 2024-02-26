package callofproject.dev.service.interview.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class CodingInterviewService implements ICodingService
{
    private final CodingInterviewCallbackService m_callbackService;

    public CodingInterviewService(CodingInterviewCallbackService callbackService)
    {
        m_callbackService = callbackService;
    }

    @Override
    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        var codingInterview = doForDataService(() -> m_callbackService.createCodeInterview(dto),
                "CodingInterviewService::createCodeInterview");

        // send notification to Owner and Participants
        if (codingInterview.getStatusCode() == Status.CREATED)
        {
            //........
        }

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId)
    {
        var codingInterview = doForDataService(() -> m_callbackService.deleteCodeInterview(ownerId, codeInterviewId),
                "CodingInterviewService::createCodeInterview");

        // send notification to Owner and Participants
        if (codingInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId)
    {
        var codingInterview = doForDataService(() -> m_callbackService.deleteCodeInterviewByProjectId(projectId),
                "CodingInterviewService::deleteCodeInterviewByProjectId");

        // send notification to Owner and Participants
        if (codingInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.addParticipant(codeInterviewId, userId),
                "CodingInterviewService::addParticipant");

        // send notification to Owner and Participants
        if (result.getStatusCode() == Status.OK)
        {
            //........
        }

        return result;
    }

    @Override
    public ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.addParticipantByProjectId(projectId, userId),
                "CodingInterviewService::addParticipantByProjectId");

        // send notification to Owner and Participants
        if (result.getStatusCode() == Status.OK)
        {
            //........
        }

        return result;
    }

    @Override
    public ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.removeParticipant(codeInterviewId, userId),
                "CodingInterviewService::removeParticipant");

        // send notification to Owner and Participants
        if (result.getStatusCode() == Status.OK)
        {
            //........
        }

        return result;
    }

    @Override
    public ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.removeParticipantByProjectId(projectId, userId),
                "CodingInterviewService::removeParticipantByProjectId");

        // send notification to Owner and Participants
        if (result.getStatusCode() == Status.OK)
        {
            //........
        }

        return result;
    }

    @Override
    public MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId)
    {
        return doForDataService(() -> m_callbackService.getParticipants(codeInterviewId), "CodingInterviewService::getParticipants");
    }

    @Override
    public MultipleResponseMessage<Object> getParticipantsByProjectId(UUID projectId)
    {
        return doForDataService(() -> m_callbackService.getParticipantsByProjectId(projectId), "CodingInterviewService::getParticipantsByProjectId");
    }

    @Override
    public ResponseMessage<Object> getInterviewByProjectId(UUID projectId)
    {
        return doForDataService(() -> m_callbackService.getInterviewByProjectId(projectId), "CodingInterviewService::getInterviewByProjectId");
    }

    @Override
    public ResponseMessage<Object> getInterview(UUID codeInterviewId)
    {
        return doForDataService(() -> m_callbackService.getInterview(codeInterviewId), "CodingInterviewService::getInterview");
    }

    @Override
    public MultipleResponseMessage<Object> getAllInterviews()
    {
        return doForDataService(m_callbackService::getAllInterviews, "CodingInterviewService::getAllInterviews");
    }
}
