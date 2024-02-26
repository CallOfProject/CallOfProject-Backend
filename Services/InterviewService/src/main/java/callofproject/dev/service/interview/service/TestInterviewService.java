package callofproject.dev.service.interview.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.interview.dto.coding.CreateTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.AssignMultipleInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class TestInterviewService
{
    private final TestInterviewCallbackService m_callbackService;

    public TestInterviewService(TestInterviewCallbackService callbackService)
    {
        m_callbackService = callbackService;
    }

    public ResponseMessage<Object> createInterview(CreateTestDTO dto)
    {
        var testInterview = doForDataService(() -> m_callbackService.createInterview(dto),
                "TestInterviewService::createCodeInterview");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.CREATED)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> addQuestion(CreateQuestionDTO createQuestionDTO)
    {
        var question = doForDataService(() -> m_callbackService.addQuestion(createQuestionDTO),
                "TestInterviewService::addQuestion");

        // send notification to Owner and Participants
        if (question.getStatusCode() == Status.CREATED)
        {
            //........
        }

        return question;
    }

    public ResponseMessage<Object> assignTestInterview(String interviewId, String userId)
    {
        var testInterview = doForDataService(() -> m_callbackService.assignTestInterview(interviewId, userId),
                "TestInterviewService::assignTestInterview");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.CREATED)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> assignMultipleTestInterview(AssignMultipleInterviewDTO dto)
    {
        var testInterview = doForDataService(() -> m_callbackService.assignMultipleTestInterview(dto),
                "TestInterviewService::assignMultipleTestInterview");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.CREATED)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> deleteTestInterview(String interviewId)
    {
        var testInterview = doForDataService(() -> m_callbackService.deleteTestInterview(interviewId),
                "TestInterviewService::deleteTestInterview");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> deleteTestInterviewByProjectId(String projectId)
    {
        var testInterview = doForDataService(() -> m_callbackService.deleteTestInterviewByProjectId(projectId),
                "TestInterviewService::deleteTestInterviewByProjectId");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto)
    {
        var testInterview = doForDataService(() -> m_callbackService.finishTestInterview(dto),
                "TestInterviewService::finishTestInterview");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> startTestInterview(String interviewId)
    {
        var testInterview = doForDataService(() -> m_callbackService.startTestInterview(interviewId),
                "TestInterviewService::startTestInterview");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> startTestInterviewByProjectId(String projectId)
    {
        var testInterview = doForDataService(() -> m_callbackService.startTestInterviewByProjectId(projectId),
                "TestInterviewService::startTestInterviewByProjectId");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> submitAnswer(CreateTestInterviewDTO dto)
    {
        var testInterview = doForDataService(() -> m_callbackService.submitAnswer(dto),
                "TestInterviewService::submitAnswer");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
    }

    public ResponseMessage<Object> getQuestion(String interviewId, int q)
    {
        return doForDataService(() -> m_callbackService.getQuestion(interviewId, q), "TestInterviewService::getQuestion");
    }

    public ResponseMessage<Object> getQuestionByProjectId(String projectId, int q)
    {
        return doForDataService(() -> m_callbackService.getQuestionByProjectId(projectId, q), "TestInterviewService::getQuestionByProjectId");
    }

    public ResponseMessage<Object> deleteQuestion(String questionId)
    {
        return doForDataService(() -> m_callbackService.deleteQuestion(questionId), "TestInterviewService::deleteQuestion");
    }

    public MultipleResponseMessage<Object> getQuestions(String interviewId)
    {
        return doForDataService(() -> m_callbackService.getQuestions(interviewId), "TestInterviewService::getQuestions");
    }

    public MultipleResponseMessage<Object> getQuestionsByProjectId(String projectId)
    {
        return doForDataService(() -> m_callbackService.getQuestionsByProjectId(projectId), "TestInterviewService::getQuestionsByProjectId");
    }
}
