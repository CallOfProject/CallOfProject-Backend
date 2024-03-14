package callofproject.dev.service.interview.service.codinginterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dsa.Pair;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.interview.config.kafka.KafkaProducer;
import callofproject.dev.service.interview.dto.InterviewResultDTO;
import callofproject.dev.service.interview.dto.UserEmailDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.coding.CreateCodingInterviewDTO;
import callofproject.dev.service.interview.service.EInterviewStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
@SuppressWarnings("unchecked")
public class CodingInterviewInterviewService implements ICodingInterviewService
{
    private final CodingInterviewCallbackService m_callbackService;
    private final KafkaProducer m_kafkaProducer;

    public CodingInterviewInterviewService(CodingInterviewCallbackService callbackService, KafkaProducer kafkaProducer)
    {
        m_callbackService = callbackService;
        m_kafkaProducer = kafkaProducer;
    }

    @Override
    public ResponseMessage<Object> addParticipant(UUID codeInterviewId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.addParticipant(codeInterviewId, userId), "CodingInterviewService::addParticipant");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.ASSIGNED);

        return result;
    }

    @Override
    public ResponseMessage<Object> addParticipantByProjectId(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.addParticipantByProjectId(projectId, userId), "CodingInterviewService::addParticipantByProjectId");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.ASSIGNED);

        return result;
    }

    @Override
    public ResponseMessage<Object> acceptInterview(UUID id, boolean isAccepted)
    {
        var result = doForDataService(() -> m_callbackService.acceptInterview(id, isAccepted), "CodingInterviewService::acceptInterview");

        if (result.getStatusCode() == Status.OK)
        {
            var dto = (InterviewResultDTO) result.getObject();
            m_kafkaProducer.sendEmail(new EmailTopic(EmailType.PROJECT_INVITATION, dto.email(), "Interview Feedback", dto.message(), null));
        }

        return result;
    }

    @Override
    public ResponseMessage<Object> createCodeInterview(CreateCodingInterviewDTO dto)
    {
        var codingInterview = doForDataService(() -> m_callbackService.createCodeInterview(dto), "CodingInterviewService::createCodeInterview");

        if (codingInterview.getStatusCode() == Status.CREATED)
        {
            var object = (Pair<CodingInterviewDTO, List<UserEmailDTO>>) codingInterview.getObject();
            m_callbackService.sendNotification(object.getFirst(), EInterviewStatus.CREATED);
            m_callbackService.sendEmails(object.getFirst(), object.getSecond(), "create_interview.html");
        }

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> deleteCodeInterview(UUID ownerId, UUID codeInterviewId)
    {
        var codingInterview = doForDataService(() -> m_callbackService.deleteCodeInterview(ownerId, codeInterviewId), "CodingInterviewService::createCodeInterview");

        if (codingInterview.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) codingInterview.getObject(), EInterviewStatus.REMOVED);

        return codingInterview;
    }

    @Override
    public ResponseMessage<Object> deleteCodeInterviewByProjectId(UUID projectId)
    {
        var codingInterview = doForDataService(() -> m_callbackService.deleteCodeInterviewByProjectId(projectId), "CodingInterviewService::deleteCodeInterviewByProjectId");

        if (codingInterview.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) codingInterview.getObject(), EInterviewStatus.REMOVED);

        return codingInterview;
    }

    @Override
    public MultipleResponseMessage<Object> findUserInterviewInformation(UUID userId)
    {
        return doForDataService(() -> m_callbackService.findUserInterviewInformation(userId), "CodingInterviewService::findUserInterviewInformation");
    }

    @Override
    public MultipleResponseMessage<Object> getParticipants(UUID codeInterviewId)
    {
        return doForDataService(() -> m_callbackService.getParticipants(codeInterviewId), "CodingInterviewService::getParticipants");
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
    public MultipleResponseMessage<Object> getParticipantsByProjectId(UUID projectId)
    {
        return doForDataService(() -> m_callbackService.getParticipantsByProjectId(projectId), "CodingInterviewService::getParticipantsByProjectId");
    }

    @Override
    public ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.isUserSolvedBefore(userId, interviewId), "CodingInterviewService::isUserSolvedBefore");
    }

    @Override
    public ResponseMessage<Object> removeParticipant(UUID codeInterviewId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.removeParticipant(codeInterviewId, userId), "CodingInterviewService::removeParticipant");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.CANCELLED);

        return result;
    }

    @Override
    public ResponseMessage<Object> removeParticipantByProjectId(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> m_callbackService.removeParticipantByProjectId(projectId, userId), "CodingInterviewService::removeParticipantByProjectId");

        if (result.getStatusCode() == Status.OK)
            m_callbackService.sendNotification((CodingInterviewDTO) result.getObject(), EInterviewStatus.CANCELLED);

        return result;
    }

    @Override
    public ResponseMessage<Object> submitInterview(UUID userId, UUID codeInterviewId, MultipartFile file)
    {
        return doForDataService(() -> m_callbackService.submitInterview(userId, codeInterviewId, file), "CodingInterviewService::submitInterview");
    }
}