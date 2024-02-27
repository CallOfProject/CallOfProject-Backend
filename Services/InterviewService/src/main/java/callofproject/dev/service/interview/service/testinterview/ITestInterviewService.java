package callofproject.dev.service.interview.service.testinterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.dto.test.AssignMultipleInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;

import java.util.UUID;

public interface ITestInterviewService
{
    ResponseMessage<Object> createInterview(CreateTestDTO dto);

    ResponseMessage<Object> addQuestion(CreateQuestionDTO createQuestionDTO);

    ResponseMessage<Object> assignTestInterview(UUID interviewId, UUID userId);

    ResponseMessage<Object> assignMultipleTestInterview(AssignMultipleInterviewDTO dto);

    ResponseMessage<Object> deleteTestInterview(UUID interviewId);

    ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId);

    ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto);

    ResponseMessage<Object> startTestInterview(UUID interviewId);

    ResponseMessage<Object> startTestInterviewByProjectId(UUID projectId);

    ResponseMessage<Object> submitAnswer(UUID interviewId, long questionId);

    ResponseMessage<Object> getQuestion(UUID interviewId, int q);

    ResponseMessage<Object> getQuestionByProjectId(UUID projectId, int q);

    ResponseMessage<Object> deleteQuestion(long questionId);

    MultipleResponseMessage<Object> getQuestions(UUID interviewId);

    MultipleResponseMessage<Object> getQuestionsByProjectId(UUID projectId);
}
