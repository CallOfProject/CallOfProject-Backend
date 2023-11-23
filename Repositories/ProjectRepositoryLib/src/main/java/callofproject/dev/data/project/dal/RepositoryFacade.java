package callofproject.dev.data.project.dal;

import callofproject.dev.data.project.repository.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.REPOSITORY_FACADE_BEAN;

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

    public RepositoryFacade(IDegreeRepository degreeRepository, IInterviewTypeRepository interviewTypeRepository,
                            IProjectAccessTypeRepository projectAccessTypeRepository, IProjectLevelRepository projectLevelRepository,
                            IProjectProfessionLevelRepository professionLevelRepository, IProjectRepository projectRepository,
                            ISectorRepository sectorRepository)
    {
        m_degreeRepository = degreeRepository;
        m_interviewTypeRepository = interviewTypeRepository;
        m_projectAccessTypeRepository = projectAccessTypeRepository;
        m_projectLevelRepository = projectLevelRepository;
        m_professionLevelRepository = professionLevelRepository;
        m_projectRepository = projectRepository;
        m_sectorRepository = sectorRepository;
    }


}
