package callofproject.dev.data.project.dal;

import callofproject.dev.data.project.repository.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.*;

@Component(REPOSITORY_FACADE_BEAN)
@Lazy
public class RepositoryFacade
{
    public final IDegreeRepository m_degreeRepository;
    public final IInterviewTypeRepository m_interviewTypeRepository;
    public final IProjectAccessTypeRepository m_projectAccessTypeRepository;
    public final IProjectLevelRepository m_projectLevelRepository;
    public final IProjectProfessionLevelRepository m_professionLevelRepository;
    public final IProjectRepository m_projectRepository;
    public final ISectorRepository m_sectorRepository;
    public final IUserRepository m_userRepository;
    public final IProjectParticipantRepository m_projectParticipantRepository;

    public RepositoryFacade(@Qualifier(DEGREE_REPOSITORY) IDegreeRepository degreeRepository,
                            @Qualifier(INTERVIEW_TYPE_REPOSITORY) IInterviewTypeRepository interviewTypeRepository,
                            @Qualifier(PROJECT_ACCESS_TYPE_REPOSITORY) IProjectAccessTypeRepository projectAccessTypeRepository,
                            @Qualifier(PROJECT_LEVEL_REPOSITORY) IProjectLevelRepository projectLevelRepository,
                            @Qualifier(PROJECT_PROFESSION_LEVEL_REPOSITORY) IProjectProfessionLevelRepository professionLevelRepository,
                            @Qualifier(PROJECT_REPOSITORY) IProjectRepository projectRepository,
                            @Qualifier(SECTOR_REPOSITORY) ISectorRepository sectorRepository,
                            @Qualifier(USER_REPOSITORY) IUserRepository userRepository,
                            @Qualifier(PROJECT_PARTICIPANT_REPOSITORY_BEAN) IProjectParticipantRepository projectParticipantRepository)
    {
        m_degreeRepository = degreeRepository;
        m_interviewTypeRepository = interviewTypeRepository;
        m_projectAccessTypeRepository = projectAccessTypeRepository;
        m_projectLevelRepository = projectLevelRepository;
        m_professionLevelRepository = professionLevelRepository;
        m_projectRepository = projectRepository;
        m_sectorRepository = sectorRepository;
        m_userRepository = userRepository;
        m_projectParticipantRepository = projectParticipantRepository;
    }
}