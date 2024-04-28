package callofproject.dev.data.community.dal;

import callofproject.dev.data.community.entity.*;
import callofproject.dev.data.community.repository.*;
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
    private final IProjectRepository m_projectRepository;
    private final IProjectParticipantRepository m_projectParticipantRepository;
    private final IUserConnectionRepository m_userConnectionRepository;
    private final IConnectionRequestRepository m_connectionRequestRepository;
    private final IBlockConnectionRepository m_blockConnectionRepository;

    public CommunityServiceHelper(IUserRepository userRepository, IProjectRepository projectRepository,
                                  IProjectParticipantRepository projectParticipantRepository,
                                  IUserConnectionRepository userConnectionRepository,
                                  IConnectionRequestRepository connectionRequestRepository,
                                  IBlockConnectionRepository blockConnectionRepository)
    {
        m_userRepository = userRepository;
        m_projectRepository = projectRepository;
        m_projectParticipantRepository = projectParticipantRepository;
        m_userConnectionRepository = userConnectionRepository;
        m_connectionRequestRepository = connectionRequestRepository;
        m_blockConnectionRepository = blockConnectionRepository;
    }

    // ------------------- Upsert -------------------
    public User upsertUser(User user)
    {
        return doForRepository(() -> m_userRepository.save(user), "CommunityServiceHelper::upsertUser");
    }

    public Project upsertProject(Project project)
    {
        return doForRepository(() -> m_projectRepository.save(project), "CommunityServiceHelper::upsertProject");
    }

    public ProjectParticipant upsertProjectParticipant(ProjectParticipant projectParticipant)
    {
        return doForRepository(() -> m_projectParticipantRepository.save(projectParticipant), "CommunityServiceHelper::upsertProjectParticipant");
    }

    public UserConnection upsertUserConnection(UserConnection userConnection)
    {
        return doForRepository(() -> m_userConnectionRepository.save(userConnection), "CommunityServiceHelper::upsertUserConnection");
    }

    public void upsertConnectionRequest(ConnectionRequest request)
    {
        doForRepository(() -> m_connectionRequestRepository.save(request), "CommunityServiceHelper::upsertConnectionRequest");
    }

    public void upsertBlockConnection(BlockConnection blockConnection)
    {
        doForRepository(() -> m_blockConnectionRepository.save(blockConnection), "CommunityServiceHelper::upsertBlockConnection");
    }

    public void upsertUserConnections(Iterable<UserConnection> userConnections)
    {
        doForRepository(() -> m_userConnectionRepository.saveAll(userConnections), "CommunityServiceHelper::upsertUserConnections");
    }


    public void deleteUserConnection(User user, UserConnection userConnection) {
        user.removeUserConnection(userConnection);
        doForRepository(() -> m_userConnectionRepository.delete(userConnection), "CommunityServiceHelper::deleteUserConnection");
    }
    // ------------------- Upsert -------------------
    //--------------------------------------------------------------------------------
    // ------------------- Remove -------------------
    public void deleteUser(User user)
    {
        doForRepository(() -> m_userRepository.delete(user), "CommunityServiceHelper::deleteUser");
    }

    public void deleteProject(Project project)
    {
        doForRepository(() -> m_projectRepository.delete(project), "CommunityServiceHelper::deleteProject");
    }

    public void deleteProjectParticipant(ProjectParticipant projectParticipant)
    {
        doForRepository(() -> m_projectParticipantRepository.delete(projectParticipant), "CommunityServiceHelper::deleteProjectParticipant");
    }

    public void deleteUserConnection(UserConnection userConnection)
    {
        doForRepository(() -> m_userConnectionRepository.delete(userConnection), "CommunityServiceHelper::deleteUserConnection");
    }

    public void deleteConnectionRequest(ConnectionRequest request)
    {
        doForRepository(() -> m_connectionRequestRepository.delete(request), "CommunityServiceHelper::deleteConnectionRequest");
    }

    public void deleteBlockConnection(BlockConnection blockConnection)
    {
        doForRepository(() -> m_blockConnectionRepository.delete(blockConnection), "CommunityServiceHelper::deleteBlockConnection");
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


    public Optional<Project> findCommunityByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_projectRepository.findById(projectId), "CommunityServiceHelper::findProjectById");
    }

    public Iterable<UserConnection> findUserConnectionsByUserId(UUID userId)
    {
        return doForRepository(() -> m_userConnectionRepository.findUserConnectionsByUserId(userId), "CommunityServiceHelper::findUserConnectionsByUserId");
    }

    public Iterable<ConnectionRequest> findConnectionRequestsByUserId(UUID userId)
    {
        return doForRepository(() -> m_connectionRequestRepository.findConnectionRequestsByRequesterId(userId), "CommunityServiceHelper::findConnectionRequestsByUserId");
    }

    public Iterable<BlockConnection> findBlockedConnectionsByUserId(UUID userId)
    {
        return doForRepository(() -> m_blockConnectionRepository.findBlockedConnectionsByUserId(userId), "CommunityServiceHelper::findBlockedConnectionsByUserId");
    }

    // ------------------- Find -------------------

    public Iterable<User> findAllUsers()
    {
        return doForRepository(m_userRepository::findAll, "CommunityServiceHelper::findAllUsers");
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
