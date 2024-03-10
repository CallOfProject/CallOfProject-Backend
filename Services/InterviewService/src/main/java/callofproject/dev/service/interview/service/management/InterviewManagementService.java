package callofproject.dev.service.interview.service.management;

import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.TestInterview;
import callofproject.dev.data.interview.entity.UserTestInterviews;
import callofproject.dev.service.interview.dto.CodingAndTestInterviewsDTO;
import callofproject.dev.service.interview.dto.ProjectDTO;
import callofproject.dev.service.interview.dto.UserTestInterviewDTO;
import callofproject.dev.service.interview.dto.test.QuestionAnswerDTO;
import callofproject.dev.service.interview.dto.test.TestInterviewDTO;
import callofproject.dev.service.interview.mapper.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.stream.StreamSupport.stream;

@Service
@Lazy
public class InterviewManagementService
{
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final ITestInterviewMapper m_testInterviewMapper;
    private final ICodingInterviewMapper m_codingInterviewMapper;
    private final IUserCodingInterviewMapper m_userCodingInterviewMapper;
    private final IUserTestInterviewMapper m_userTestInterviewMapper;
    private final IUserMapper m_userMapper;
    private final IProjectMapper m_projectMapper;

    public InterviewManagementService(InterviewServiceHelper interviewServiceHelper, ITestInterviewMapper testInterviewMapper, ICodingInterviewMapper codingInterviewMapper, IUserCodingInterviewMapper userCodingInterviewMapper, IUserTestInterviewMapper userTestInterviewMapper, IUserMapper userMapper, IProjectMapper projectMapper)
    {
        m_interviewServiceHelper = interviewServiceHelper;
        m_testInterviewMapper = testInterviewMapper;
        m_codingInterviewMapper = codingInterviewMapper;
        m_userCodingInterviewMapper = userCodingInterviewMapper;
        m_userTestInterviewMapper = userTestInterviewMapper;
        m_userMapper = userMapper;
        m_projectMapper = projectMapper;
    }

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


    public ResponseMessage<Object> findTestInterviewOwner(UUID interviewId)
    {
        var testInterview = m_interviewServiceHelper.findTestInterviewById(interviewId);

        if (testInterview.isEmpty())
            return new ResponseMessage<>("Test Interview not found!", Status.NOT_FOUND, null);

        var projectDTO = m_projectMapper.toProjectDTO(testInterview.get().getProject());
        var testInterviewDTO = m_testInterviewMapper.toTestInterviewDTO(testInterview.get(), projectDTO);


        var userTestInterviewList = testInterview.get().getTestInterviews().stream()
                .map(uti -> toUserTestInterviewDTO(uti, testInterview.get(), projectDTO, testInterviewDTO)).toList();

        return new ResponseMessage<>("Coding Interview found!", Status.OK, userTestInterviewList);
    }

    private UserTestInterviewDTO toUserTestInterviewDTO(UserTestInterviews uti, TestInterview testInterview, ProjectDTO projectDTO, TestInterviewDTO testInterviewDTO)
    {
        var userId = uti.getUser().getUserId();

        var userAnswers = uti.getAnswers().stream()
                .map(qa -> new QuestionAnswerDTO(userId, testInterview.getId(), qa.getQuestionId(), qa.getAnswer()))
                .toList();

        return m_userTestInterviewMapper.toUserTestInterviewDTO(uti, testInterviewDTO, userAnswers, projectDTO, m_userMapper.toUserDTO(uti.getUser()));
    }
}
