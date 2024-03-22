package callofproject.dev.service.interview;

import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.ProjectParticipant;
import callofproject.dev.data.interview.repository.*;
import callofproject.dev.service.interview.mapper.ICodingInterviewMapper;
import callofproject.dev.service.interview.service.codinginterview.CodingInterviewCallbackService;
import callofproject.dev.service.interview.service.management.InterviewManagementCallbackService;
import callofproject.dev.service.interview.service.testinterview.TestInterviewCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public class Injection
{
    @Autowired
    private CodingInterviewCallbackService m_codingInterviewService;

    @Autowired
    private TestInterviewCallbackService m_testInterviewService;

    @Autowired
    private InterviewManagementCallbackService m_managementService;

    @Autowired
    private IUserRepository m_userRepository;

    @Autowired
    private IProjectRepository m_projectRepository;

    @Autowired
    private ICodingInterviewRepository m_codingInterviewRepository;

    @Autowired
    private InterviewServiceHelper m_interviewServiceHelper;

    @Autowired
    private IProjectParticipantRepository m_participantRepository;

    @Autowired
    private ITestInterviewRepository m_testInterviewRepository;

    public ITestInterviewRepository getTestInterviewRepository()
    {
        return m_testInterviewRepository;
    }

    public IProjectParticipantRepository getParticipantRepository()
    {
        return m_participantRepository;
    }

    public InterviewServiceHelper getInterviewServiceHelper()
    {
        return m_interviewServiceHelper;
    }

    public ICodingInterviewRepository getCodingInterviewRepository()
    {
        return m_codingInterviewRepository;
    }

    public IProjectRepository getProjectRepository()
    {
        return m_projectRepository;
    }

    public CodingInterviewCallbackService getCodingInterviewService()
    {
        return m_codingInterviewService;
    }

    public TestInterviewCallbackService getTestInterviewService()
    {
        return m_testInterviewService;
    }

    public InterviewManagementCallbackService getManagementService()
    {
        return m_managementService;
    }

    public IUserRepository getUserRepository()
    {
        return m_userRepository;
    }
}
