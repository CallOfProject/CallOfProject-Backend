package callofproject.dev.data.community.dal;

import callofproject.dev.data.community.entity.Community;
import callofproject.dev.data.community.entity.Project;
import callofproject.dev.data.community.entity.ProjectParticipant;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.data.community.repository.ICommunityRepository;
import callofproject.dev.data.community.repository.IProjectParticipantRepository;
import callofproject.dev.data.community.repository.IProjectRepository;
import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
public class CommunityServiceHelper
{
    private final IUserRepository m_userRepository;
    private final ICommunityRepository m_communityRepository;
    private final IProjectRepository m_projectRepository;
    private final IProjectParticipantRepository m_projectParticipantRepository;

    public CommunityServiceHelper(IUserRepository userRepository, ICommunityRepository communityRepository, IProjectRepository projectRepository, IProjectParticipantRepository projectParticipantRepository)
    {
        m_userRepository = userRepository;
        m_communityRepository = communityRepository;
        m_projectRepository = projectRepository;
        m_projectParticipantRepository = projectParticipantRepository;
    }

    // ------------------- Upsert -------------------
    public User upsertUser(User user)
    {
        return doForRepository(() -> m_userRepository.save(user), "CommunityServiceHelper::upsertUser");
    }

    public Community upsertCommunity(Community community)
    {
        return doForRepository(() -> m_communityRepository.save(community), "CommunityServiceHelper::upsertCommunity");
    }

    public Project upsertProject(Project project)
    {
        return doForRepository(() -> m_projectRepository.save(project), "CommunityServiceHelper::upsertProject");
    }

    public ProjectParticipant upsertProjectParticipant(ProjectParticipant projectParticipant)
    {
        return doForRepository(() -> m_projectParticipantRepository.save(projectParticipant), "CommunityServiceHelper::upsertProjectParticipant");
    }

    // ------------------- Upsert -------------------
    //--------------------------------------------------------------------------------
    // ------------------- Remove -------------------
    public void deleteUser(User user)
    {
        doForRepository(() -> m_userRepository.delete(user), "CommunityServiceHelper::deleteUser");
    }

    public void deleteCommunity(Community community)
    {
        doForRepository(() -> m_communityRepository.delete(community), "CommunityServiceHelper::deleteCommunity");
    }

    public void deleteProject(Project project)
    {
        doForRepository(() -> m_projectRepository.delete(project), "CommunityServiceHelper::deleteProject");
    }

    public void deleteProjectParticipant(ProjectParticipant projectParticipant)
    {
        doForRepository(() -> m_projectParticipantRepository.delete(projectParticipant), "CommunityServiceHelper::deleteProjectParticipant");
    }

    // ------------------- Remove -------------------
    //--------------------------------------------------------------------------------
    // ------------------- Find -------------------
    public Optional<User> findUserByUsername(String username)
    {
        return doForRepository(() -> m_userRepository.findByUsername(username), "CommunityServiceHelper::findUserByUsername");
    }

    public Optional<User> findUserById(UUID userId)
    {
        return doForRepository(() -> m_userRepository.findById(userId), "CommunityServiceHelper::findUserById");
    }

    public Optional<Project> findProjectById(UUID projectId)
    {
        return doForRepository(() -> m_projectRepository.findById(projectId), "CommunityServiceHelper::findProjectById");
    }

    public Optional<Community> findCommunityById(UUID communityId)
    {
        return doForRepository(() -> m_communityRepository.findById(communityId), "CommunityServiceHelper::findCommunityById");
    }


    public Optional<Project> findCommunityByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_projectRepository.findById(projectId), "CommunityServiceHelper::findProjectById");
    }

    // ------------------- Find -------------------

    public Iterable<Community> findAllCommunities()
    {
        return doForRepository(m_communityRepository::findAll, "CommunityServiceHelper::findAllCommunities");
    }

    public Iterable<User> findAllUsers()
    {
        return doForRepository(m_userRepository::findAll, "CommunityServiceHelper::findAllUsers");
    }

    public Iterable<Community> findAllCommunitiesByCommunityNameContainsIgnoreCase(String communityName)
    {
        return doForRepository(() -> m_communityRepository.findAllByCommunityNameContainsIgnoreCase(communityName),
                "CommunityServiceHelper::findAllCommunitiesByCommunityNameContainsIgnoreCase");
    }

    public Iterable<Community> findAllCommunitiesByExactCommunityName(String communityName, String communityType)
    {
        return doForRepository(() -> m_communityRepository.findAllByExactCommunityName(communityName, communityType),
                "CommunityServiceHelper::findAllCommunitiesByExactCommunityName");
    }


    public Optional<ProjectParticipant> findProjectParticipantByProjectIdAndUserId(UUID projectId, UUID userId)
    {
        return doForRepository(() -> m_projectParticipantRepository.findProjectParticipantByProjectIdAndUserId(projectId, userId),
                "CommunityServiceHelper::findProjectParticipantByProjectIdAndUserId");
    }

    public void removeParticipant(ProjectParticipant projectParticipant)
    {
        doForRepository(() -> m_projectParticipantRepository.delete(projectParticipant), "CommunityServiceHelper::removeParticipant");
    }

    public void saveParticipant(ProjectParticipant projectParticipant)
    {
        doForRepository(() -> m_projectParticipantRepository.save(projectParticipant), "CommunityServiceHelper::saveParticipant");
    }

    public void deleteProjectById(UUID projectId)
    {
        doForRepository(() -> m_projectRepository.deleteById(projectId), "CommunityServiceHelper::deleteProjectById");
    }

    public Project saveProject(Project project)
    {
        return doForRepository(() -> m_projectRepository.save(project), "CommunityServiceHelper::saveProject");
    }
}
