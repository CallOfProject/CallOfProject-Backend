package callofproject.dev.data.task.dal;

import callofproject.dev.data.task.entity.*;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.data.task.repository.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
public class TaskServiceHelper
{
    private final IProjectRepository m_projectRepository;
    private final IProjectParticipantRepository m_participantRepository;
    private final IUserRepository m_userRepository;
    private final ITaskRepository m_taskRepository;
    private final IRoleRepository m_roleRepository;

    public TaskServiceHelper(IProjectRepository projectRepository, IProjectParticipantRepository participantRepository,
                             IUserRepository userRepository, ITaskRepository taskRepository,
                             IRoleRepository roleRepository)
    {
        m_projectRepository = projectRepository;
        m_participantRepository = participantRepository;
        m_userRepository = userRepository;
        m_taskRepository = taskRepository;
        m_roleRepository = roleRepository;
    }

    public Project saveProject(Project project)
    {
        return doForRepository(() -> m_projectRepository.save(project), "Failed to save project");
    }

    public ProjectParticipant saveParticipant(ProjectParticipant participant)
    {
        return doForRepository(() -> m_participantRepository.save(participant), "Failed to save participant");
    }

    public User saveUser(User user)
    {
        return doForRepository(() -> m_userRepository.save(user), "Failed to save user");
    }

    public Task saveTask(Task task)
    {
        return doForRepository(() -> m_taskRepository.save(task), "Failed to save task");
    }

    public Role saveRole(Role role)
    {
        return doForRepository(() -> m_roleRepository.save(role), "Failed to save role");
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void deleteProject(Project project)
    {
        doForRepository(() -> m_projectRepository.delete(project), "Failed to delete project");
    }

    public void deleteParticipant(ProjectParticipant participant)
    {
        doForRepository(() -> m_participantRepository.delete(participant), "Failed to delete participant");
    }

    public void deleteUser(User user)
    {
        doForRepository(() -> m_userRepository.delete(user), "Failed to delete user");
    }

    public void deleteTask(Task task)
    {
        doForRepository(() -> m_taskRepository.delete(task), "Failed to delete task");
    }

    public void deleteRole(Role role)
    {
        doForRepository(() -> m_roleRepository.delete(role), "Failed to delete role");
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Optional<User> findUserByUsername(String username)
    {
        return doForRepository(() -> m_userRepository.findByUsername(username), "Failed to find user by username");
    }

    public Optional<User> findUserById(UUID id)
    {
        return doForRepository(() -> m_userRepository.findById(id), "Failed to find user by id");
    }

    public Optional<Project> findProjectById(UUID id)
    {
        return doForRepository(() -> m_projectRepository.findById(id), "Failed to find project by id");
    }

    public Optional<ProjectParticipant> findParticipantById(UUID id)
    {
        return doForRepository(() -> m_participantRepository.findById(id), "Failed to find participant by id");
    }

    public Optional<Task> findTaskById(UUID id)
    {
        return doForRepository(() -> m_taskRepository.findById(id), "Failed to find task by id");
    }

    public Optional<Role> findRoleById(long id)
    {
        return doForRepository(() -> m_roleRepository.findById(id), "Failed to find role by id");
    }
    // -----------------------------------------------------------------------------------------------------------------

    public Iterable<Task> findAllTasksByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_taskRepository.findAllByProjectId(projectId), "Failed to find all tasks by project id in repository");
    }

    public Iterable<Task> findAllByStartDateAndEndDate(LocalDate startDate, LocalDate endDate)
    {
        return doForRepository(() -> m_taskRepository.findAllByStartDateAndEndDate(startDate, endDate), "Failed to find all tasks by start date and end date in repository");
    }

    public Iterable<Task> findAllByStartDate(LocalDate startDate)
    {
        return doForRepository(() -> m_taskRepository.findAllByStartDate(startDate), "Failed to find all tasks by start date in repository");
    }

    public Iterable<Task> findAllByStartDateAfter(LocalDate startDate)
    {
        return doForRepository(() -> m_taskRepository.findTasksByStartDateAfter(startDate), "Failed to find all tasks by start date after in repository");
    }

    public Iterable<Task> findAllByStartDateBefore(LocalDate startDate)
    {
        return doForRepository(() -> m_taskRepository.findTasksByStartDateBefore(startDate), "Failed to find all tasks by start date before in repository");
    }

    public Iterable<Task> findAllByEndDate(LocalDate endDate)
    {
        return doForRepository(() -> m_taskRepository.findTasksByEndDate(endDate), "Failed to find all tasks by end date in repository");
    }

    public Iterable<Task> findAllByEndDateAfter(LocalDate endDate)
    {
        return doForRepository(() -> m_taskRepository.findTasksByEndDateAfter(endDate), "Failed to find all tasks by end date after in repository");
    }

    public Iterable<Task> findAllByEndDateBefore(LocalDate endDate)
    {
        return doForRepository(() -> m_taskRepository.findTasksByEndDateBefore(endDate), "Failed to find all tasks by end date before in repository");
    }

    public Iterable<Task> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate)
    {
        return doForRepository(() -> m_taskRepository.findAllByStartDateBetween(startDate, endDate), "Failed to find all tasks by start date between in repository");
    }

    public Iterable<Task> findAllByEndDateBetween(LocalDate startDate, LocalDate endDate)
    {
        return doForRepository(() -> m_taskRepository.findAllByEndDateBetween(startDate, endDate), "Failed to find all tasks by end date between in repository");
    }

    public Iterable<Task> findAllByPriority(String priority)
    {
        return doForRepository(() -> m_taskRepository.findAllByPriority(priority), "Failed to find all tasks by priority in repository");
    }

    public Iterable<Task> findAllByTaskStatus(String taskStatus)
    {
        return doForRepository(() -> m_taskRepository.findAllByTaskStatus(taskStatus), "Failed to find all tasks by task status in repository");
    }

    public Iterable<Task> findAllByTaskStatusAndProjectProjectId(String taskStatus, UUID projectId)
    {
        return doForRepository(() -> m_taskRepository.findAllByTaskStatusAndProjectProjectId(taskStatus, projectId), "Failed to find all tasks by task status and project id in repository");
    }

    public Iterable<Task> findAllByPriorityAndProjectProjectId(String priority, UUID projectId)
    {
        return doForRepository(() -> m_taskRepository.findAllByPriorityAndProjectProjectId(priority, projectId), "Failed to find all tasks by priority and project id in repository");
    }

    public Iterable<User> findUsersByIds(List<UUID> ids)
    {
        return doForRepository(() -> m_userRepository.findAllById(ids), "Failed to find users by ids in repository");
    }
}
