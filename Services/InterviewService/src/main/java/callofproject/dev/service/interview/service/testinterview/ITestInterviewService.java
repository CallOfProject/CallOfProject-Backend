package callofproject.dev.service.interview.service.testinterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;

import java.util.UUID;

/**
 * The interface Test interview service.
 * This interface is used to handle the test interview service.
 */
public interface ITestInterviewService
{
    /**
     * Create a new test interview.
     *
     * @param dto the dto
     * @return the response message
     */
    ResponseMessage<Object> createInterview(CreateTestDTO dto);

    /**
     * Delete test interview.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    ResponseMessage<Object> deleteTestInterview(UUID interviewId);

    /**
     * Delete test interview by project id.
     *
     * @param projectId the project id
     * @return the response message
     */
    ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId);

    /**
     * Finish test interview.
     *
     * @param dto the dto
     * @return the response message
     */
    ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto);

    /**
     * Submit answer.
     *
     * @param dto the dto
     * @return the response message
     */
    ResponseMessage<Object> submitAnswer(QuestionAnswerDTO dto);

    /**
     * Get question response message.
     *
     * @param interviewId the interview id
     * @param q           the q
     * @return the response message
     */
    ResponseMessage<Object> getQuestion(UUID interviewId, int q);

    /**
     * Get question by project id response message.
     *
     * @param projectId the project id
     * @param q         the q
     * @return the response message
     */
    ResponseMessage<Object> getQuestionByProjectId(UUID projectId, int q);

    /**
     * Delete question response message.
     *
     * @param questionId the question id
     * @return the response message
     */
    ResponseMessage<Object> deleteQuestion(long questionId);

    /**
     * Submit interview response message.
     *
     * @param userId          the user id
     * @param testInterviewId the test interview id
     * @return the response message
     */
    ResponseMessage<Object> submitInterview(UUID userId, UUID testInterviewId);

    /**
     * Is user solved before response message.
     *
     * @param userId      the user id
     * @param interviewId the interview id
     * @return the response message
     */
    ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId);

    /**
     * Get interview information response message.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    ResponseMessage<Object> getInterviewInformation(UUID interviewId);

    /**
     * Accept or reject interview. for project owners
     *
     * @param id         interview id
     * @param isAccepted the is accepted
     * @return the response message
     */
    ResponseMessage<Object> acceptInterview(UUID id, boolean isAccepted);

    /**
     * Get questions response message.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    MultipleResponseMessage<Object> getQuestions(UUID interviewId);

    /**
     * Get questions by project id response message.
     *
     * @param projectId the project id
     * @return the response message
     */
    MultipleResponseMessage<Object> getQuestionsByProjectId(UUID projectId);
}