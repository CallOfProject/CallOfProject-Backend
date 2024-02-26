package callofproject.dev.service.interview.service;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.interview.data.dal.InterviewServiceHelper;
import callofproject.dev.service.interview.data.entity.*;
import callofproject.dev.service.interview.data.entity.enums.InterviewStatus;
import callofproject.dev.service.interview.dto.test.AssignMultipleInterviewDTO;
import callofproject.dev.service.interview.dto.test.CreateQuestionDTO;
import callofproject.dev.service.interview.dto.test.CreateTestDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewFinishDTO;
import callofproject.dev.service.interview.mapper.ITestInterviewMapper;
import callofproject.dev.service.interview.mapper.ITestInterviewQuestionMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Lazy
public class TestInterviewCallbackService
{
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final ITestInterviewMapper m_testInterviewMapper;
    private final ITestInterviewQuestionMapper m_testInterviewQuestionMapper;

    public TestInterviewCallbackService(InterviewServiceHelper interviewServiceHelper, ITestInterviewMapper testInterviewMapper, ITestInterviewQuestionMapper testInterviewQuestionMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_testInterviewMapper = testInterviewMapper;
        m_testInterviewQuestionMapper = testInterviewQuestionMapper;
    }

    public ResponseMessage<Object> createInterview(CreateTestDTO dto)
    {
        var project = findProjectIfExistsById(dto.projectId());
        var testInterview = m_interviewServiceHelper.createInterview(m_testInterviewMapper.toTestInterview(dto));
        testInterview.setProject(project);
        // to question entity
        var questions = dto.questions().stream().map(m_testInterviewQuestionMapper::toTestInterviewQuestion).toList();
        var savedQuestions = StreamSupport.stream(m_interviewServiceHelper.saveQuestions(questions).spliterator(), false).collect(Collectors.toSet());
        testInterview.setQuestions(savedQuestions);

        var savedTestInterview = m_interviewServiceHelper.createInterview(testInterview);

        return new ResponseMessage<>("Test interview created successfully", Status.CREATED, savedTestInterview);
    }

    public ResponseMessage<Object> addQuestion(CreateQuestionDTO createQuestionDTO)
    {
        var interview = findInterviewIfExistsById(createQuestionDTO.interviewId());
        var question = m_testInterviewQuestionMapper.toTestInterviewQuestion(createQuestionDTO);
        question.setTestInterview(interview);
        var savedQuestion = m_interviewServiceHelper.saveQuestion(question);

        return new ResponseMessage<>("Question added successfully", Status.CREATED, savedQuestion);
    }

    public ResponseMessage<Object> assignTestInterview(UUID interviewId, UUID userId)
    {
        var interview = findInterviewIfExistsById(interviewId);
        var user = findUserIfExistsById(userId);

        user.addTestInterview(interview);
        m_interviewServiceHelper.saveUser(user);

        return new ResponseMessage<>("Test interview assigned successfully", Status.OK, true);
    }

    public ResponseMessage<Object> assignMultipleTestInterview(AssignMultipleInterviewDTO dto)
    {
        var interview = findInterviewIfExistsById(dto.interviewId());
        var users = StreamSupport.stream(m_interviewServiceHelper.findUsersByIds(dto.userIds()).spliterator(), false).collect(Collectors.toSet());

        users.forEach(usr -> usr.addTestInterview(interview));
        m_interviewServiceHelper.saveUsers(users);

        return new ResponseMessage<>("Test interview assigned successfully", Status.OK, true);
    }

    public ResponseMessage<Object> deleteTestInterview(UUID interviewId)
    {
        var interview = findInterviewIfExistsById(interviewId);
        m_interviewServiceHelper.deleteTestInterview(interview);

        return new ResponseMessage<>("Test interview deleted successfully", Status.OK, true);
    }

    public ResponseMessage<Object> deleteTestInterviewByProjectId(UUID projectId)
    {
        return deleteTestInterview(findProjectIfExistsById(projectId).getTestInterview().getId());
    }

    public ResponseMessage<Object> finishTestInterview(TestInterviewFinishDTO dto)
    {
        var interview = findInterviewIfExistsById(dto.interviewId());
        interview.setInterviewStatus(InterviewStatus.FINISHED);
        m_interviewServiceHelper.createInterview(interview);

        return new ResponseMessage<>("Test interview finished successfully", Status.OK, true);
    }

    public ResponseMessage<Object> startTestInterview(UUID interviewId)
    {
        var interview = findInterviewIfExistsById(interviewId);
        interview.setInterviewStatus(InterviewStatus.STARTED);
        m_interviewServiceHelper.createInterview(interview);

        return new ResponseMessage<>("Test interview started successfully", Status.OK, true);
    }

    public ResponseMessage<Object> startTestInterviewByProjectId(UUID projectId)
    {
        var project = findProjectIfExistsById(projectId);

        return startTestInterview(project.getTestInterview().getId());
    }

    public ResponseMessage<Object> submitAnswer(UUID interviewId, long questionId)
    {
        var interview = findInterviewIfExistsById(interviewId);
        var question = interview.getQuestions().stream().filter(q -> q.getId() == questionId).findFirst().get();
        question.setStatus(QuestionStatus.ANSWERED);
        m_interviewServiceHelper.saveQuestion(question);

        return new ResponseMessage<>("Answer submitted successfully", Status.OK, true);
    }

    public ResponseMessage<Object> getQuestion(UUID interviewId, int q)
    {
        var interview = findInterviewIfExistsById(interviewId);

        if (q > interview.getQuestions().size())
            throw new DataServiceException("Question not found");

        return new ResponseMessage<>("Question retrieved successfully", Status.OK, interview.getQuestions().stream().skip(q).findFirst().get());
    }

    public ResponseMessage<Object> getQuestionByProjectId(UUID projectId, int q)
    {
        return getQuestion(findProjectIfExistsById(projectId).getTestInterview().getId(), q);
    }

    public ResponseMessage<Object> deleteQuestion(long questionId)
    {
        var question = findQuestionIfExistsById(questionId);

        m_interviewServiceHelper.deleteQuestion(question);

        return new ResponseMessage<>("Question deleted successfully", Status.OK, true);
    }


    public MultipleResponseMessage<Object> getQuestions(UUID interviewId)
    {
        var interview = findInterviewIfExistsById(interviewId);
        var questions = interview.getQuestions().stream().toList();

        return new MultipleResponseMessage<>(questions.size(), "Questions retrieved successfully", interview.getQuestions());
    }

    public MultipleResponseMessage<Object> getQuestionsByProjectId(UUID projectId)
    {
        return getQuestions(findProjectIfExistsById(projectId).getTestInterview().getId());
    }

    // private methods

    private TestInterview findInterviewIfExistsById(UUID testInterviewId)
    {
        var interview = m_interviewServiceHelper.findTestInterviewById(testInterviewId);

        if (interview.isEmpty())
            throw new DataServiceException("Interview not found");

        return interview.get();
    }


    private User findUserIfExistsById(UUID userId)
    {
        var user = m_interviewServiceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User not found");

        return user.get();
    }

    private TestInterviewQuestion findQuestionIfExistsById(long questionId)
    {
        var question = m_interviewServiceHelper.findQuestionById(questionId);

        if (question.isEmpty())
            throw new DataServiceException("Question not found");

        return question.get();
    }

    private Project findProjectIfExistsById(UUID projectId)
    {
        var project = m_interviewServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException("Project not found");

        return project.get();
    }
}
