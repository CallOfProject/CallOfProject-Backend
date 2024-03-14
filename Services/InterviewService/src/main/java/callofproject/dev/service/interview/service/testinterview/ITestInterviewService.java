package callofproject.dev.service.interview.service.testinterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;

import java.util.UUID;

public interface ITestInterviewService
{
    ResponseMessage<Object> createInterview(CreateTestDTO dto);

    ResponseMessage<Object> deleteTestInterview(UUID interviewId);

    ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId);

    ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto);

    ResponseMessage<Object> startTestInterview(UUID interviewId);

    ResponseMessage<Object> startTestInterviewByProjectId(UUID projectId);

    ResponseMessage<Object> submitAnswer(QuestionAnswerDTO dto);

    ResponseMessage<Object> getQuestion(UUID interviewId, int q);

    ResponseMessage<Object> getQuestionByProjectId(UUID projectId, int q);

    ResponseMessage<Object> deleteQuestion(long questionId);

    ResponseMessage<Object> submitInterview(UUID userId, UUID testInterviewId);

    ResponseMessage<Object> isUserSolvedBefore(UUID userId, UUID interviewId);

    ResponseMessage<Object> getInterviewInformation(UUID interviewId);

    ResponseMessage<Object> acceptInterview(UUID id, boolean isAccepted);

    MultipleResponseMessage<Object> getQuestions(UUID interviewId);

    MultipleResponseMessage<Object> getQuestionsByProjectId(UUID projectId);
}