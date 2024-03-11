package callofproject.dev.service.interview.service.testinterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dsa.Pair;
import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.service.interview.config.kafka.KafkaProducer;
import callofproject.dev.service.interview.dto.InterviewResultDTO;
import callofproject.dev.service.interview.dto.NotificationKafkaDTO;
import callofproject.dev.service.interview.dto.UserEmailDTO;
import callofproject.dev.service.interview.dto.coding.CodingInterviewDTO;
import callofproject.dev.service.interview.dto.test.*;
import callofproject.dev.service.interview.service.EInterviewStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class TestInterviewService implements ITestInterviewService
{
    private final TestInterviewCallbackService m_callbackService;
    private final KafkaProducer m_kafkaProducer;

    public TestInterviewService(TestInterviewCallbackService callbackService, KafkaProducer kafkaProducer)
    {
        m_callbackService = callbackService;
        m_kafkaProducer = kafkaProducer;
    }

    @Override
    public ResponseMessage<Object> createInterview(CreateTestDTO dto)
    {
        var testInterview = doForDataService(() -> m_callbackService.createInterview(dto), "TestInterviewService::createCodeInterview");

        if (testInterview.getStatusCode() == Status.CREATED)
        {
            var object = (Pair<TestInterviewDTO, List<UserEmailDTO>>) testInterview.getObject();
            m_callbackService.sendNotification(object.getFirst(), EInterviewStatus.CREATED);
            m_callbackService.sendEmails(object.getFirst(), object.getSecond(), "create_interview.html");
        }

        return testInterview;
    }

    @Override
    public ResponseMessage<Object> addQuestion(CreateQuestionDTO createQuestionDTO)
    {
        return doForDataService(() -> m_callbackService.addQuestion(createQuestionDTO), "TestInterviewService::addQuestion");
    }

    @Override
    public ResponseMessage<Object> deleteTestInterview(UUID interviewId)
    {
        var testInterview = doForDataService(() -> m_callbackService.deleteTestInterview(interviewId),
                "TestInterviewService::deleteTestInterview");

        if (testInterview.getStatusCode() == Status.OK)
            sendNotification((TestInterviewDTO) testInterview.getObject(), "Test Interview Deleted");

        return testInterview;
    }

    @Override
    public ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId)
    {
        var testInterview = doForDataService(() -> m_callbackService.deleteTestInterviewByProjectId(projectId),
                "TestInterviewService::deleteTestInterviewByProjectId");

        if (testInterview.getStatusCode() == Status.OK)
            sendNotification((TestInterviewDTO) testInterview.getObject(), "Test Interview Deleted");

        return testInterview;
    }

    @Override
    public ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto)
    {
        return doForDataService(() -> m_callbackService.finishTestInterview(dto), "TestInterviewService::finishTestInterview");
    }

    @Override
    public ResponseMessage<Object> startTestInterview(UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.startTestInterview(interviewId), "TestInterviewService::startTestInterview");
    }

    @Override
    public ResponseMessage<Object> startTestInterviewByProjectId(UUID projectId)
    {
        return doForDataService(() -> m_callbackService.startTestInterviewByProjectId(projectId), "TestInterviewService::startTestInterviewByProjectId");
    }

    @Override
    public ResponseMessage<Object> submitAnswer(QuestionAnswerDTO dto)
    {
        return doForDataService(() -> m_callbackService.submitAnswer(dto), "TestInterviewService::submitAnswer");
    }

    @Override
    public ResponseMessage<Object> submitInterview(UUID userId, UUID testInterviewId)
    {
        return doForDataService(() -> m_callbackService.submitInterview(testInterviewId, userId), "TestInterviewService::submitInterview");
    }


    @Override
    public ResponseMessage<Object> getQuestion(UUID interviewId, int q)
    {
        return doForDataService(() -> m_callbackService.getQuestion(interviewId, q), "TestInterviewService::getQuestion");
    }

    @Override
    public ResponseMessage<Object> getQuestionByProjectId(UUID projectId, int q)
    {
        return doForDataService(() -> m_callbackService.getQuestionByProjectId(projectId, q), "TestInterviewService::getQuestionByProjectId");
    }

    @Override
    public ResponseMessage<Object> deleteQuestion(long questionId)
    {
        return doForDataService(() -> m_callbackService.deleteQuestion(questionId), "TestInterviewService::deleteQuestion");
    }

    @Override
    public MultipleResponseMessage<Object> getQuestions(UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.getQuestions(interviewId), "TestInterviewService::getQuestions");
    }

    @Override
    public MultipleResponseMessage<Object> getQuestionsByProjectId(UUID projectId)
    {
        return doForDataService(() -> m_callbackService.getQuestionsByProjectId(projectId), "TestInterviewService::getQuestionsByProjectId");
    }


    @Override
    public ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.isUserSolvedBefore(userId, interviewId), "TestInterviewService::isUserSolvedBefore");
    }

    @Override
    public ResponseMessage<Object> getInterviewInformation(UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.getInterviewInformation(interviewId), "TestInterviewService::getInterviewInformation");
    }

    @Override
    public ResponseMessage<Object> acceptInterview(UUID id, boolean isAccepted)
    {
        var result = doForDataService(() -> m_callbackService.acceptInterview(id, isAccepted), "TestInterviewService::acceptInterview");

        if (result.getStatusCode() == Status.OK)
        {
            var dto = (InterviewResultDTO) result.getObject();
            m_kafkaProducer.sendEmail(new EmailTopic(EmailType.PROJECT_INVITATION, dto.email(), "Interview Feedback", dto.message(), null));
        }

        return result;
    }


    private void sendNotification(TestInterviewDTO dto, String title)
    {
        var ownerId = dto.projectDTO().projectId();
        var userTestInterviews = m_callbackService.findInterviewIfExistsById(UUID.fromString(dto.id()));
        var participants = userTestInterviews.getTestInterviews().stream().map(UserTestInterviews::getUser).map(User::getUserId).toList();
        var message = "Test Interview " + title + " has been created for project " + dto.projectDTO().projectName();
        participants.forEach(participant -> sendNotification(ownerId, participant, message, title));
    }

    private void sendNotification(UUID fromUserId, UUID toUserId, String message, String title)
    {
        var dto = new NotificationKafkaDTO.Builder()
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setMessage(message)
                .setNotificationTitle(title)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationDataType(NotificationDataType.INTERVIEW)
                .build();

        m_kafkaProducer.sendNotification(dto);
    }

}
