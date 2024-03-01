package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.service.EInterviewStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class CodingInterviewInterviewService implements ICodingInterviewService
{
    private final CodingInterviewCallbackService m_callbackService;

    public CodingInterviewInterviewService(CodingInterviewCallbackService callbackService)
    {
        m_callbackService = callbackService;
    }

    @Override
    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        var codingInterview = doForDataService(() -> m_callbackService.createCodeInterview(dto),
                "CodingInterviewService::createCodeInterview");

        if (codingInterview.getStatusCode() == Status.CREATED)
            m_callbackService.sendNotification((CodingInterviewDTO) codingInterview.getObject(), EInterviewStatus.CREATED);

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId)
    {
        var codingInterview = doForDataService(() -> m_callbackService.deleteCodeInterview(ownerId, codeInterviewId),
                "CodingInterviewService::createCodeInterview");

        if (codingInterview.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) codingInterview.getObject(), EInterviewStatus.REMOVED);

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId)
    {
        var codingInterview = doForDataService(() -> m_callbackService.deleteCodeInterviewByProjectId(projectId),
                "CodingInterviewService::deleteCodeInterviewByProjectId");

        if (codingInterview.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) codingInterview.getObject(), EInterviewStatus.REMOVED);

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.addParticipant(codeInterviewId, userId),
                "CodingInterviewService::addParticipant");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.ASSIGNED);

        return result;
    }

    @Override
    public ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.addParticipantByProjectId(projectId, userId),
                "CodingInterviewService::addParticipantByProjectId");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.ASSIGNED);

        return result;
    }

    @Override
    public ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.removeParticipant(codeInterviewId, userId),
                "CodingInterviewService::removeParticipant");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.CANCELLED);


        return result;
    }

    @Override
    public ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.removeParticipantByProjectId(projectId, userId),
                "CodingInterviewService::removeParticipantByProjectId");

        // send notification to Owner and Participants
        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.CANCELLED);

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
    public MultipleResponseMessage<Object> findUserInterviewInformation(UUID userId)
    {
        return doForDataService(() -> m_callbackService.findUserInterviewInformation(userId), "CodingInterviewService::findUserInterviewInformation");
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
    public ResponseMessage<Object> submitInterview(UUID userId, UUID codeInterviewId, MultipartFile file)
    {
        return doForDataService(() -> m_callbackService.submitInterview(userId, codeInterviewId, file), "CodingInterviewService::submitInterview");
    }

    @Override
    public ResponseMessage<Object> runTests(MultipartFile file)
    {
        throw new UnsupportedOperationException("Unsupported operation for this project!");
    }
}
