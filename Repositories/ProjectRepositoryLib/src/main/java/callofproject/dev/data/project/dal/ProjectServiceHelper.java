/**
 * ProjectServiceHelper.java created at 11.12.2023 22:01:59
 * by CallOfProjectTeam
 */

package callofproject.dev.data.project.dal;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.library.exception.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.data.project.ProjectRepositoryBeanName.REPOSITORY_FACADE_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component(PROJECT_SERVICE_HELPER_BEAN)
@PropertySource("classpath:application-project_repository.properties")
//@SuppressWarnings("all")
@Lazy
public class ProjectServiceHelper
{
    @Value("${project.page.default-size}")
    private int m_defaultPageSize;
    private final RepositoryFacade m_facade;

    public ProjectServiceHelper(@Qualifier(REPOSITORY_FACADE_BEAN) RepositoryFacade facade)
    {
        m_facade = facade;
    }

    public Page<Project> findAllProjectsPageable(int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAll(pageable),
                "ProjectServiceHelper::findAllProjects");
    }


    public Iterable<Project> findAllProjects()
    {
        return doForRepository(() -> m_facade.m_projectRepository.findAll(), "ProjectServiceHelper::findAllProjects");
    }


    public Optional<Project> findProjectById(UUID projectId)
    {
        return doForRepository(() -> m_facade.m_projectRepository.findById(projectId),
                "ProjectServiceHelper::findById");
    }
    public ProjectParticipant addProjectParticipant(ProjectParticipant participant)
    {
        return doForRepository(() -> m_facade.m_projectParticipantRepository.save(participant),
                "ProjectServiceHelper::addProjectParticipant");
    }
    public boolean addProjectParticipant(UUID projectId, UUID userId)
    {
        var user = findUserById(userId);
        var project = findProjectById(projectId);

        if (user.isEmpty() || project.isEmpty())
            throw new RepositoryException("User or Project is not found!");

        project.get().addProjectParticipant(user.get());

        var updatedProject = doForRepository(() -> m_facade.m_projectRepository.save(project.get()),
                "ProjectServiceHelper::addProjectParticipant");

        return updatedProject != null;
    }


    public Optional<ProjectParticipant> findProjectParticipantByUserIdAndProjectId(UUID userId, UUID projectId)
    {
        return doForRepository(() -> m_facade.m_projectParticipantRepository.findProjectParticipantByUserIdAndProjectId(userId, projectId),
                "ProjectServiceHelper::findProjectParticipantByUserIdAndProjectId");
    }

    public User addUser(User user)
    {
        return doForRepository(() -> m_facade.m_userRepository.save(user), "ProjectServiceHelper::addUser");
    }

    public void removeParticipantRequestByRequestId(UUID participantRequestId)
    {
        doForRepository(() -> m_facade.m_projectParticipantRequestRepository.deleteById(participantRequestId),
                "ProjectServiceHelper::removeParticipantRequest");
    }

    public void deleteProjectParticipant(ProjectParticipant participant)
    {
        doForRepository(() -> m_facade.m_projectParticipantRepository.delete(participant),
                "ProjectServiceHelper::deleteProjectParticipant");
    }

    public Optional<ProjectParticipantRequest> findProjectParticipantRequestByParticipantRequestId(UUID projectParticipantRequestId)
    {
        return doForRepository(() -> m_facade.m_projectParticipantRequestRepository.findById(projectParticipantRequestId),
                "ProjectServiceHelper::findProjectParticipantRequestByParticipantRequestId");
    }

    public Optional<User> findUserById(UUID userId)
    {
        return doForRepository(() -> m_facade.m_userRepository.findById(userId),
                "ProjectServiceHelper::findUserById");
    }

    public Iterable<User> saveAllUsers(Iterable<User> users)
    {
        return doForRepository(() -> m_facade.m_userRepository.saveAll(users), "ProjectServiceHelper::saveAllUsers");
    }

    public void removeProjectById(UUID projectId)
    {
        doForRepository(() -> m_facade.m_projectRepository.deleteById(projectId), "ProjectServiceHelper::removeProjectById");
    }

    public Iterable<ProjectParticipant> findAllProjectParticipantByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_facade.m_projectParticipantRepository.findAllByProjectId(projectId),
                "ProjectServiceHelper::findAllProjectParticipantByProjectId");
    }

    public void removeAllParticipantRequests(List<ProjectParticipantRequest> requests)
    {
        doForRepository(() -> m_facade.m_projectParticipantRequestRepository.deleteAll(requests),
                "ProjectServiceHelper::removeAllParticipantRequests");
    }

    public ProjectParticipantRequest sendParticipantRequestToProject(ProjectParticipantRequest participantRequest)
    {

        return doForRepository(() -> m_facade.m_projectParticipantRequestRepository.save(participantRequest),
                "ProjectServiceHelper::sendParticipantRequestToProject");
    }

   /* public Optional<ProjectParticipantRequest> sendParticipantRequestToProject(UUID projectId, UUID userId)
    {
        var user = findUserById(userId);
        var project = findProjectById(projectId);

        if (user.isEmpty() || project.isEmpty())
            throw new RepositoryException("User or Project is not found!");

        project.get().addProjectParticipantRequest(user.get());

        var updatedProject = doForRepository(() -> m_facade.m_projectRepository.save(project.get()), "ProjectServiceHelper::sendParticipantRequestToProject");

        return updatedProject.getProjectParticipantRequests().stream().filter(r -> r.getProject().getProjectId().equals(projectId) &&
                r.getUser().getUserId().equals(userId)).findFirst();
    }
*/
    public Page<Project> findAllValidProjects(int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository
                        .findAllByProjectStatusAndAdminOperationStatusAndProjectAccessType(pageable),
                "ProjectServiceHelper::findAllValidProjects");
    }

    public Page<Project> findAllParticipantProjectByUserId(UUID userId, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllParticipantProjectByUserId(userId, pageable),
                "ProjectServiceHelper::findAllParticipantProjectByUsername");
    }

    public Page<Project> findAllProjectByProjectOwnerUserId(UUID userId, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectOwnerId(userId, pageable),
                "ProjectServiceHelper::findAllProjectByProjectOwnerUserId");
    }

    public Optional<User> findUserByUsername(String username)
    {
        return doForRepository(() -> m_facade.m_userRepository.findByUsername(username),
                "ProjectServiceHelper::findUserByUsername");
    }

    public Project saveProject(Project project)
    {
        return doForRepository(() -> m_facade.m_projectRepository.save(project),
                "ProjectServiceHelper::saveProject");
    }

    public void removeUser(UUID userId)
    {
        doForRepository(() -> m_facade.m_userRepository.deleteById(userId), "ProjectServiceHelper::removeUser");
    }

    public Iterable<Project> findAllProjectByIds(List<UUID> projectIds)
    {
        return doForRepository(() -> m_facade.m_projectRepository.findAllById(projectIds),
                "ProjectServiceHelper::findAllProjectByIds");
    }
}