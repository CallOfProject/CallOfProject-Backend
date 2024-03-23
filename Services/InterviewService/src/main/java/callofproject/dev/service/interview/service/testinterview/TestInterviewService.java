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
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;
import callofproject.dev.service.interview.service.EInterviewStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * @author Nuri Can ÖZTÜRK
 * The type Test interview service.
 * This class is used to handle the test interview service.
 */
@Service
@Lazy
@SuppressWarnings("unchecked")
public class TestInterviewService implements ITestInterviewService
{
    private final TestInterviewCallbackService m_callbackService;
    private final KafkaProducer m_kafkaProducer;

    /**
     * Instantiates a new Test interview service.
     *
     * @param callbackService the callback service
     * @param kafkaProducer   the kafka producer
     */
    public TestInterviewService(TestInterviewCallbackService callbackService, KafkaProducer kafkaProducer)
    {
        m_callbackService = callbackService;
        m_kafkaProducer = kafkaProducer;
    }

    /**
     * Create interview response message.
     *
     * @param dto the dto
     * @return the response message
     */
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

    /**
     * Delete interview response message.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> deleteTestInterview(UUID interviewId)
    {
        var testInterview = doForDataService(() -> m_callbackService.deleteTestInterview(interviewId),
                "TestInterviewService::deleteTestInterview");

        if (testInterview.getStatusCode() == Status.OK)
            sendNotification((TestInterviewDTO) testInterview.getObject());

        return testInterview;
    }

    /**
     * Delete interview response message.
     *
     * @param projectId the project id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId)
    {
        var testInterview = doForDataService(() -> m_callbackService.deleteTestInterviewByProjectId(projectId),
                "TestInterviewService::deleteTestInterviewByProjectId");

        if (testInterview.getStatusCode() == Status.OK)
            sendNotification((TestInterviewDTO) testInterview.getObject());

        return testInterview;
    }

    /**
     * Finish test interview response message.
     *
     * @param dto the dto
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto)
    {
        return doForDataService(() -> m_callbackService.finishTestInterview(dto), "TestInterviewService::finishTestInterview");
    }


    /**
     * Submit question with QuestionAnswerDTO
     *
     * @param dto the QuestionAnswerDTO
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> submitAnswer(QuestionAnswerDTO dto)
    {
        return doForDataService(() -> m_callbackService.submitAnswer(dto), "TestInterviewService::submitAnswer");
    }

    /**
     * Submit interview response message.
     *
     * @param userId          the user id
     * @param testInterviewId the test interview id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> submitInterview(UUID userId, UUID testInterviewId)
    {
        return doForDataService(() -> m_callbackService.submitInterview(testInterviewId, userId), "TestInterviewService::submitInterview");
    }


    /**
     * Get interview response message.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> getQuestion(UUID interviewId, int q)
    {
        return doForDataService(() -> m_callbackService.getQuestion(interviewId, q), "TestInterviewService::getQuestion");
    }

    /**
     * Get interview response message.
     *
     * @param projectId the project id
     * @param q         the q
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> getQuestionByProjectId(UUID projectId, int q)
    {
        return doForDataService(() -> m_callbackService.getQuestionByProjectId(projectId, q), "TestInterviewService::getQuestionByProjectId");
    }

    /**
     * Delete question
     *
     * @param questionId the question id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> deleteQuestion(long questionId)
    {
        return doForDataService(() -> m_callbackService.deleteQuestion(questionId), "TestInterviewService::deleteQuestion");
    }

    /**
     * Find all questions
     *
     * @param interviewId the interview id
     * @return the response message
     */
    @Override
    public MultipleResponseMessage<Object> getQuestions(UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.getQuestions(interviewId), "TestInterviewService::getQuestions");
    }

    /**
     * Find all questions
     *
     * @param projectId the project id
     * @return the response message
     */
    @Override
    public MultipleResponseMessage<Object> getQuestionsByProjectId(UUID projectId)
    {
        return doForDataService(() -> m_callbackService.getQuestionsByProjectId(projectId), "TestInterviewService::getQuestionsByProjectId");
    }


    /**
     * Check if user solved before
     *
     * @param interviewId the interview id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.isUserSolvedBefore(userId, interviewId), "TestInterviewService::isUserSolvedBefore");
    }

    /**
     * Get interview information for start interview
     *
     * @param interviewId the interview id
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> getInterviewInformation(UUID interviewId)
    {
        return doForDataService(() -> m_callbackService.getInterviewInformation(interviewId), "TestInterviewService::getInterviewInformation");
    }


    /**
     * Accept interview.
     *
     * @param id         interview id
     * @param isAccepted the is accepted
     * @return the response message
     */
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


    private void sendNotification(TestInterviewDTO dto)
    {
        var ownerId = dto.projectDTO().projectId();
        var userTestInterviews = m_callbackService.findInterviewIfExistsById(UUID.fromString(dto.id()));
        var participants = userTestInterviews.getTestInterviews().stream().map(UserTestInterviews::getUser).map(User::getUserId).toList();
        var message = "Test Interview Deleted" + " has been created for project " + dto.projectDTO().projectName();
        participants.forEach(participant -> sendNotification(ownerId, participant, message));
    }

    private void sendNotification(UUID fromUserId, UUID toUserId, String message)
    {
        var dto = new NotificationKafkaDTO.Builder()
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setMessage(message)
                .setNotificationTitle("Test Interview Deleted")
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationDataType(NotificationDataType.INTERVIEW)
                .build();

        m_kafkaProducer.sendNotification(dto);
    }
}