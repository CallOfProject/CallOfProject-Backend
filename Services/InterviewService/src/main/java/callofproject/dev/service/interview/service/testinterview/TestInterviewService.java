package callofproject.dev.service.interview.service.testinterview;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.service.interview.dto.test.AssignMultipleInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service
@Lazy
public class TestInterviewService implements ITestInterviewService
{
    private final TestInterviewCallbackService m_callbackService;

    public TestInterviewService(TestInterviewCallbackService callbackService)
    {
        m_callbackService = callbackService;
    }

    @Override
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

    @Override
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

    @Override
    public ResponseMessage<Object> assignTestInterview(UUID interviewId, UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseMessage<Object> assignMultipleTestInterview(AssignMultipleInterviewDTO dto)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseMessage<Object> deleteTestInterview(UUID interviewId)
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

    @Override
    public ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId)
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

    @Override
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

    @Override
    public ResponseMessage<Object> startTestInterview(UUID interviewId)
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

    @Override
    public ResponseMessage<Object> startTestInterviewByProjectId(UUID projectId)
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

    @Override
    public ResponseMessage<Object> submitAnswer(UUID interviewId, long questionId)
    {
        var testInterview = doForDataService(() -> m_callbackService.submitAnswer(interviewId, questionId),
                "TestInterviewService::submitAnswer");

        // send notification to Owner and Participants
        if (testInterview.getStatusCode() == Status.OK)
        {
            //........
        }

        return testInterview;
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

}
