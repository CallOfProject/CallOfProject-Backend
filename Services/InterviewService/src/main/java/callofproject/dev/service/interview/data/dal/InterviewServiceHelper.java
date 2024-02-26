package callofproject.dev.service.interview.data.dal;

import callofproject.dev.service.interview.data.entity.CodingInterview;
import callofproject.dev.service.interview.data.entity.Project;
import callofproject.dev.service.interview.data.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
public class InterviewServiceHelper
{
    private final RepositoryFacade m_repositoryFacade;

    public InterviewServiceHelper(RepositoryFacade repositoryFacade)
    {
        m_repositoryFacade = repositoryFacade;
    }


    public CodingInterview createCodeInterview(CodingInterview codingInterview)
    {
        return doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.save(codingInterview), "Error creating coding interview");
    }

    public void deleteCodeInterview(CodingInterview codingInterview)
    {
        doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.delete(codingInterview), "Error deleting coding interview");
    }


    public Optional<CodingInterview> findInterviewById(UUID codeInterviewId)
    {
        return doForRepository(() -> m_repositoryFacade.m_codingInterviewRepository.findById(codeInterviewId), "Error finding coding interview by id");
    }

    public Optional<User> findUserById(UUID userId)
    {
        return doForRepository(() -> m_repositoryFacade.m_userRepository.findById(userId), "Error finding user by id");
    }

    public Optional<Project> findProjectById(UUID projectId)
    {
        return doForRepository(() -> m_repositoryFacade.m_projectRepository.findById(projectId), "Error finding project by id");
    }

    public Iterable<CodingInterview> findAllInterviews()
    {
        return doForRepository(m_repositoryFacade.m_codingInterviewRepository::findAll, "Error finding all coding interviews");
    }
}
