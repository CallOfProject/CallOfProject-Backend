package callofproject.dev.data.project.dal;

import callofproject.dev.data.project.repository.IProjectParticipantRepository;
import callofproject.dev.data.project.repository.IProjectParticipantRequestRepository;
import callofproject.dev.data.project.repository.IProjectRepository;
import callofproject.dev.data.project.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.*;

@Component(REPOSITORY_FACADE_BEAN)
@Lazy
public class RepositoryFacade
{
    public final IProjectRepository m_projectRepository;
    public final IUserRepository m_userRepository;
    public final IProjectParticipantRepository m_projectParticipantRepository;

    public final IProjectParticipantRequestRepository m_projectParticipantRequestRepository;

    public RepositoryFacade(
            @Qualifier(PROJECT_REPOSITORY) IProjectRepository projectRepository,
            @Qualifier(USER_REPOSITORY) IUserRepository userRepository,
            @Qualifier(PROJECT_PARTICIPANT_REPOSITORY_BEAN) IProjectParticipantRepository projectParticipantRepository,
            @Qualifier(PROJECT_PARTICIPANT_REQUEST_REPOSITORY) IProjectParticipantRequestRepository projectParticipantRequestRepository)
    {
        m_projectRepository = projectRepository;
        m_userRepository = userRepository;
        m_projectParticipantRepository = projectParticipantRepository;
        m_projectParticipantRequestRepository = projectParticipantRequestRepository;
    }
}