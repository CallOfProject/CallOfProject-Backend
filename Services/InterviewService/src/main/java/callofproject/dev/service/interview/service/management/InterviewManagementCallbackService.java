package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.QuestionAnswer;
import callofproject.dev.data.interview.entity.TestInterview;
import callofproject.dev.data.interview.entity.TestInterviewQuestion;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.service.interview.dto.CodingAndTestInterviewsDTO;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.UserTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import callofproject.dev.service.interview.mapper.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.StreamSupport.stream;

/**
 * @author Nuri Can ÖZTÜRK
 * Represents the class interview management service logic.
 * CopyRight(C) 2023 by Call Of Project Teams.
 */
@Service
@Lazy
public class InterviewManagementCallbackService
{
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final ITestInterviewMapper m_testInterviewMapper;
    private final ICodingInterviewMapper m_codingInterviewMapper;
    private final IUserCodingInterviewMapper m_userCodingInterviewMapper;
    private final IUserTestInterviewMapper m_userTestInterviewMapper;
    private final IUserMapper m_userMapper;
    private final IProjectMapper m_projectMapper;

    /**
     * Instantiates a new Interview management callback service.
     *
     * @param interviewServiceHelper    the interview service helper
     * @param testInterviewMapper       the test interview mapper
     * @param codingInterviewMapper     the coding interview mapper
     * @param userCodingInterviewMapper the user coding interview mapper
     * @param userTestInterviewMapper   the user test interview mapper
     * @param userMapper                the user mapper
     * @param projectMapper             the project mapper
     */
    public InterviewManagementCallbackService(InterviewServiceHelper interviewServiceHelper, ITestInterviewMapper testInterviewMapper, ICodingInterviewMapper codingInterviewMapper, IUserCodingInterviewMapper userCodingInterviewMapper, IUserTestInterviewMapper userTestInterviewMapper, IUserMapper userMapper, IProjectMapper projectMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_testInterviewMapper = testInterviewMapper;
        m_codingInterviewMapper = codingInterviewMapper;
        m_userCodingInterviewMapper = userCodingInterviewMapper;
        m_userTestInterviewMapper = userTestInterviewMapper;
        m_userMapper = userMapper;
        m_projectMapper = projectMapper;
    }

    /**
     * Find all interviews by user id.
     *
     * @param userId the user id
     * @return the multiple response message
     */
    public MultipleResponseMessage<Object> findAllInterviewsByUserId(UUID userId)
    {
        var codingInterviews = stream(m_interviewServiceHelper.findCodingInterviewsByOwnerId(userId).spliterator(), false)
                .map(ci -> m_codingInterviewMapper.toCodingInterviewDTO(ci, m_projectMapper.toProjectDTO(ci.getProject())))
                .toList();

        var testInterviews = stream(m_interviewServiceHelper.findTestInterviewsByOwnerId(userId).spliterator(), false)
                .map(ti -> m_testInterviewMapper.toTestInterviewDTO(ti, m_projectMapper.toProjectDTO(ti.getProject()))).toList();
        var itemCount = codingInterviews.size() + testInterviews.size();

        var codingWithTestInterviewsDTO = new CodingAndTestInterviewsDTO(codingInterviews, testInterviews);

        return new MultipleResponseMessage<>(itemCount, "Coding And Test Interviews found!", codingWithTestInterviewsDTO);
    }

    /**
     * Find coding interview owner response message.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    public ResponseMessage<Object> findCodingInterviewOwner(UUID interviewId)
    {
        var codingInterview = m_interviewServiceHelper.findCodingInterviewById(interviewId);

        if (codingInterview.isEmpty())
            return new ResponseMessage<>("Coding Interview not found!", Status.NOT_FOUND, null);

        var projectDTO = m_projectMapper.toProjectDTO(codingInterview.get().getProject());
        var codingInterviewDTO = m_codingInterviewMapper.toCodingInterviewDTO(codingInterview.get(), projectDTO);

        var userCodingInterviewList = codingInterview.get().getCodingInterviews().stream()
                .map(uci -> m_userCodingInterviewMapper.toUserCodingInterviewDTOV2(uci, codingInterviewDTO, projectDTO, m_userMapper.toUserDTO(uci.getUser()))).toList();

        return new ResponseMessage<>("Coding Interview found!", Status.OK, userCodingInterviewList);
    }


    /**
     * Find test interview owner response message.
     *
     * @param interviewId the interview id
     * @return the response message
     */
    public ResponseMessage<Object> findTestInterviewOwner(UUID interviewId)
    {
        var testInterview = m_interviewServiceHelper.findTestInterviewById(interviewId);

        if (testInterview.isEmpty())
            return new ResponseMessage<>("Test Interview not found!", Status.NOT_FOUND, null);

        var projectDTO = m_projectMapper.toProjectDTO(testInterview.get().getProject());
        var testInterviewDTO = m_testInterviewMapper.toTestInterviewDTO(testInterview.get(), projectDTO);


        var userTestInterviewList = testInterview.get().getTestInterviews().stream()
                .map(uti -> toUserTestInterviewDTO(uti, testInterview.get(), projectDTO, testInterviewDTO)).toList();

        return new ResponseMessage<>("Test Interview found!", Status.OK, userTestInterviewList);
    }


    private TestInterviewQuestion getQuestion(List<TestInterviewQuestion> questions, long questionId)
    {
        return questions.stream().filter(q -> q.getId() == questionId).findFirst().get();
    }

    private QuestionAnswerDTO toQuestionAnswerDTO(UUID userId, UUID interviewId, QuestionAnswer qa, List<TestInterviewQuestion> questions)
    {
        var q = getQuestion(questions, qa.getQuestionId());
        return new QuestionAnswerDTO(userId, interviewId, qa.getQuestionId(), q.getQuestion(), qa.getAnswer(), q.getOption1(),
                q.getOption2(), q.getOption3(), q.getOption4(), q.getAnswer());
    }

    /**
     * To user test interview dto user test interview dto.
     *
     * @param uti              the uti
     * @param testInterview    the test interview
     * @param projectDTO       the project dto
     * @param testInterviewDTO the test interview dto
     * @return the user test interview dto
     */
    public UserTestInterviewDTO toUserTestInterviewDTO(UserTestInterviews uti, TestInterview testInterview, ProjectDTO projectDTO, TestInterviewDTO testInterviewDTO)
    {
        var userId = uti.getUser().getUserId();

        var questions = testInterview.getQuestions().stream().toList();
        var userAnswers = uti.getAnswers().stream().map(qa -> toQuestionAnswerDTO(userId, testInterview.getId(), qa, questions)).toList();

        return m_userTestInterviewMapper.toUserTestInterviewDTO(uti, testInterviewDTO, userAnswers, projectDTO, m_userMapper.toUserDTO(uti.getUser()));
    }
}
