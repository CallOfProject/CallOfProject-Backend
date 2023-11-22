package callofproject.dev.repository.repository.project.dal;

import callofproject.dev.repository.repository.project.repository.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
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
    public final ITagProjectRepository m_tagProjectRepository;
    public final IUserTagRepository m_userTagRepository;

    public RepositoryFacade(IDegreeRepository degreeRepository, IInterviewTypeRepository interviewTypeRepository,
                            IProjectAccessTypeRepository projectAccessTypeRepository, IProjectLevelRepository projectLevelRepository,
                            IProjectProfessionLevelRepository professionLevelRepository, IProjectRepository projectRepository,
                            ISectorRepository sectorRepository, ITagProjectRepository tagProjectRepository, IUserTagRepository userTagRepository)
    {
        m_degreeRepository = degreeRepository;
        m_interviewTypeRepository = interviewTypeRepository;
        m_projectAccessTypeRepository = projectAccessTypeRepository;
        m_projectLevelRepository = projectLevelRepository;
        m_professionLevelRepository = professionLevelRepository;
        m_projectRepository = projectRepository;
        m_sectorRepository = sectorRepository;
        m_tagProjectRepository = tagProjectRepository;
        m_userTagRepository = userTagRepository;
    }


}
