package callofproject.dev.service.interview.data.dal;

import callofproject.dev.service.interview.data.repository.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class RepositoryFacade
{
    public final IUserRepository m_userRepository;
    public final IProjectRepository m_projectRepository;
    public final ITestInterviewRepository m_testInterviewRepository;
    public final ICodingInterviewRepository m_codingInterviewRepository;
    public final IProjectParticipantRepository m_projectParticipantRepository;
    public final ITestInterviewQuestionRepository m_testInterviewQuestionRepository;
    public final IUserCodingInterviewsRepository m_userCodingInterviewsRepository;
    public final IUserTestInterviewsRepository m_userTestInterviewsRepository;

    public RepositoryFacade(IUserRepository userRepository, IProjectRepository projectRepository, IProjectParticipantRepository projectParticipantRepository,
                            ITestInterviewRepository testInterviewRepository, ITestInterviewQuestionRepository testInterviewQuestionRepository,
                            ICodingInterviewRepository codingInterviewRepository, IUserCodingInterviewsRepository userCodingInterviewsRepository, IUserTestInterviewsRepository userTestInterviewsRepository)
    {
        m_userRepository = userRepository;
        m_projectRepository = projectRepository;
        m_projectParticipantRepository = projectParticipantRepository;
        m_testInterviewRepository = testInterviewRepository;
        m_testInterviewQuestionRepository = testInterviewQuestionRepository;
        m_codingInterviewRepository = codingInterviewRepository;
        m_userCodingInterviewsRepository = userCodingInterviewsRepository;
        m_userTestInterviewsRepository = userTestInterviewsRepository;
    }
}
