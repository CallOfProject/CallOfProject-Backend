package callofproject.dev.service.interview.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.service.interview.data.dal.InterviewServiceHelper;
import callofproject.dev.service.interview.dto.coding.TestInterviewSubmitAnswerDTO;
import callofproject.dev.service.interview.dto.test.AssignMultipleInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class TestInterviewService
{
    private final InterviewServiceHelper m_interviewServiceHelper;

    public TestInterviewService(InterviewServiceHelper interviewServiceHelper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
    }

    public ResponseMessage<Object> createInterview(CreateTestDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> addQuestion(CreateQuestionDTO createQuestionDTO)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> assignTestInterview(String interviewId, String userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> assignMultipleTestInterview(AssignMultipleInterviewDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> deleteTestInterview(String interviewId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> deleteTestInterviewByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> startTestInterview(String interviewId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> startTestInterviewByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> submitAnswer(TestInterviewSubmitAnswerDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> getQuestion(String interviewId, int q)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> getQuestionByProjectId(String projectId, int q)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<Object> deleteQuestion(String questionId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public MultipleResponseMessage<Object> getQuestions(String interviewId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public MultipleResponseMessage<Object> getQuestionsByProjectId(String projectId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
