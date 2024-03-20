package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dsa.Pair;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.task.config.TaskFilterSpecifications;
import callofproject.dev.task.config.kafka.KafkaProducer;
import callofproject.dev.task.dto.NotificationKafkaDTO;
import callofproject.dev.task.dto.NotificationObject;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.*;
import callofproject.dev.task.dto.response.TaskDTO;
import callofproject.dev.task.mapper.IProjectMapper;
import callofproject.dev.task.mapper.ITaskMapper;
import callofproject.dev.task.mapper.IUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.task.config.TaskFilterSpecifications.*;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toSet;

/**
 * The TaskServiceCallback class provides methods for handling task-related operations.
 */
@Service
@Lazy
public class TaskServiceCallback
{
    private final TaskServiceHelper m_taskServiceHelper;
    private final KafkaProducer m_kafkaProducer;
    private final ObjectMapper m_objectMapper;
    private final ITaskMapper m_taskMapper;
    private final IProjectMapper m_projectMapper;
    private final IUserMapper m_userMapper;

    /**
     * Constructs a new TaskServiceCallback with the provided dependencies.
     *
     * @param taskServiceHelper The TaskServiceHelper instance used for data access operations.
     * @param kafkaProducer     The KafkaProducer instance used for sending Kafka messages.
     * @param objectMapper      The ObjectMapper instance used for mapping objects to JSON.
     * @param taskMapper        The ITaskMapper instance used for mapping Task objects to TaskDTO objects.
     * @param projectMapper     The IProjectMapper instance used for mapping Project objects to ProjectDTO objects.
     * @param userMapper        The IUserMapper instance used for mapping User objects to UserDTO objects.
     */
    public TaskServiceCallback(TaskServiceHelper taskServiceHelper, KafkaProducer kafkaProducer, ObjectMapper objectMapper, ITaskMapper taskMapper, IProjectMapper projectMapper, IUserMapper userMapper)
    {
        m_taskServiceHelper = taskServiceHelper;
        m_kafkaProducer = kafkaProducer;
        m_objectMapper = objectMapper;
        m_taskMapper = taskMapper;
        m_projectMapper = projectMapper;
        m_userMapper = userMapper;
    }

    /**
     * Creates a new task with the provided data.
     *
     * @param createTaskDTO The data required to create a new task.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<Object> createTaskCallback(CreateTaskDTO createTaskDTO)
    {
        // Check if start date is after end date
        if (createTaskDTO.startDate().isAfter(createTaskDTO.endDate()))
            throw new DataServiceException("Start date cannot be after end date");

        // Find project by id
        var project = findProjectByIdIfExist(createTaskDTO.projectId());
        // Find users by ids
        var users = toStream(m_taskServiceHelper.findUsersByIds(createTaskDTO.userIds())).collect(toSet());
        var taskObj = m_taskMapper.toTask(createTaskDTO, project, users);
        // Save task
        var savedTask = m_taskServiceHelper.saveTask(taskObj);

        return new ResponseMessage<>("Task created successfully", Status.CREATED, savedTask);
    }


    /**
     * Changes the status of a task with the provided data.
     *
     * @param dto The data required to change the status of a task.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<Object> changeTaskStatusCallback(ChangeTaskStatusDTO dto)
    {
        // Find task by id
        var task = findTaskByIdIfExist(dto.taskId());

        // Check if the user is the owner of the project
        if (task.getProject().getProjectOwner().getUserId().equals(dto.userId()))
            throw new DataServiceException("You are not the owner of this project");

        // Change task status
        task.setTaskStatus(dto.status());

        // Update task
        var updatedTask = m_taskServiceHelper.saveTask(task);

        return new ResponseMessage<>("Task status changed successfully", Status.OK, new Pair<>("Task status changed successfully", updatedTask));
    }


    /**
     * Deletes a task with the provided id.
     *
     * @param taskId The id of the task to be deleted.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<Object> deleteTaskCallback(UUID taskId)
    {
        // Find task by id
        var task = findTaskByIdIfExist(taskId);
        // Get task assignees
        var taskAssignees = task.getAssignees();

        // Remove task from assignees
        if (taskAssignees != null && !taskAssignees.isEmpty())
            taskAssignees.forEach(user -> user.getAssignedTasks().remove(task));

        // Delete task
        m_taskServiceHelper.deleteTask(task);

        return new ResponseMessage<>("Task deleted successfully", Status.OK, task);
    }

    /**
     * Deletes a task with the provided id and user id.
     *
     * @param taskId The id of the task to be deleted.
     * @param userId The id of the user to be removed from the task.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<?> deleteTaskByTaskIdAndUserIdCallback(UUID taskId, UUID userId)
    {
        // Find task by id
        var task = findTaskByIdIfExist(taskId);
        // Get task assignees
        var taskAssignees = task.getAssignees();
        // Find user by id
        var user = taskAssignees.stream().filter(u -> u.getUserId().equals(userId)).findFirst();

        // Check if user is assigned to the task
        if (user.isEmpty())
            throw new DataServiceException("User not found");

        // Remove user from task
        task.getAssignees().remove(user.get());

        // Save task
        var savedTask = m_taskServiceHelper.saveTask(task);

        // Delete task if no assignees left
        if (savedTask.getAssignees().isEmpty())
            m_taskServiceHelper.deleteTask(savedTask);

        return new ResponseMessage<>("You removed from task successfully!", Status.OK, task);
    }

    /**
     * Updates a task with the provided data.
     *
     * @param updateTaskDTO The data required to update a task.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<Object> updateTaskCallback(UpdateTaskDTO updateTaskDTO)
    {
        // Find task by id
        var task = findTaskByIdIfExist(updateTaskDTO.taskId());

        // Check if start date is after end date
        if (updateTaskDTO.startDate().isAfter(updateTaskDTO.endDate()))
            throw new DataServiceException("Start date cannot be after end date");
        // Update task
        task.setPriority(updateTaskDTO.priority());
        task.setTaskStatus(updateTaskDTO.taskStatus());
        task.setTitle(updateTaskDTO.title());
        task.setDescription(updateTaskDTO.description());
        task.setStartDate(updateTaskDTO.startDate());
        task.setEndDate(updateTaskDTO.endDate());

        var users = toStream(m_taskServiceHelper.findUsersByIds(updateTaskDTO.userIds())).collect(toSet());
        task.setAssignees(users);

        var updatedTask = doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::updateTaskCallback");

        return new ResponseMessage<>("Task updated successfully", Status.OK, new Pair<>(task, toTaskDTO(updatedTask)));
    }


    /**
     * Changes the priority of a task with the provided data.
     *
     * @param dto The data required to change the priority of a task.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<Object> changeTaskPriorityCallback(ChangeTaskPriorityDTO dto)
    {
        // Find task by id
        var task = findTaskByIdIfExist(dto.taskId());

        // Check if the user is the owner of the project
        if (task.getProject().getProjectOwner().getUserId().equals(dto.userId()))
            throw new DataServiceException("You are not the owner of this project");

        // Change task priority
        task.setPriority(dto.priority());

        // Update task
        var updatedTask = m_taskServiceHelper.saveTask(task);

        return new ResponseMessage<>("Task priority changed successfully", Status.OK, new Pair<>("Task priority changed successfully", updatedTask));
    }

    /**
     * Finds all assignees of a task with the provided id.
     *
     * @param userId The id of the user making the request.
     * @param taskId The id of the task to find assignees for.
     * @return A list of UserDTOs containing the assignees of the task.
     */
    public List<UserDTO> findAllAssigneesByTaskIdCallback(UUID userId, UUID taskId)
    {
        // Find task by id
        var task = findTaskByIdIfExist(taskId);

        // Check if the user is the owner of the project
        if (!task.getProject().getProjectOwner().getUserId().equals(userId))
            throw new DataServiceException("You are not the owner of this project");

        return task.getAssignees().stream().map(m_userMapper::toUserDTO).toList();
    }

    /**
     * Finds all tasks of a project with the provided id.
     *
     * @param projectId The id of the project to find tasks for.
     * @param page      The page number of the tasks to find.
     * @return A MultipleResponseMessagePageable containing the result of the operation.
     */
    public MultipleResponseMessagePageable<?> findTasksByProjectIdCallback(UUID projectId, int page)
    {
        // Find tasks by project id
        var taskPage = m_taskServiceHelper.findAllTasksByProjectId(projectId, page);

        // Convert tasks to TaskDTOs
        var tasks = doForDataService(() -> taskPage.getContent().stream()
                .map(this::toTaskDTO)
                .sorted(TaskDTO::compareTo)
                .toList(), "TaskService::findTasksByProjectId");

        return toMultipleResponseMessagePageable(taskPage.getTotalPages(), page, taskPage.getNumberOfElements(), tasks);
    }

    /**
     * Finds all tasks of a project with the provided id and user id.
     *
     * @param projectId The id of the project to find tasks for.
     * @param userId    The id of the user to find tasks for.
     * @param page      The page number of the tasks to find.
     * @return A MultipleResponseMessagePageable containing the result of the operation.
     */
    public MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserIdCallback(UUID projectId, UUID userId, int page)
    {
        // Find tasks by project id
        var taskPage = m_taskServiceHelper.findAllTasksByProjectId(projectId, page);

        // Convert tasks to TaskDTOs
        var tasks = doForDataService(() -> taskPage.getContent().stream()
                .filter(t -> t.getAssignees().stream().anyMatch(ta -> ta.getUserId().equals(userId)))
                .map(this::toTaskDTO)
                .sorted(TaskDTO::compareTo)
                .toList(), "TaskService::findTasksByProjectIdAndUserId");

        return toMultipleResponseMessagePageable(taskPage.getTotalPages(), page, taskPage.getNumberOfElements(), tasks);
    }


    /**
     * Finds all tasks by the provided filter.
     *
     * @param dto  The filter to apply to the tasks.
     * @param page The page number of the tasks to find.
     * @return A MultipleResponseMessagePageable containing the result of the operation.
     */
    public MultipleResponseMessagePageable<?> findTaskByFilterCallback(TaskFilterDTO dto, int page)
    {
        // Create a specification for the filter
        var spec = Specification.where(TaskFilterSpecifications.hasPriority(dto.priority()))
                .and(hasTaskStatus(dto.taskStatus()))
                .and(hasStartDate(dto.startDate()))
                .and(hasFinishDate(dto.finishDate()))
                .and(hasProjectId(dto.projectId()))
                .and(hasProjectOwnerId(dto.projectOwnerId()));

        // Find tasks by filter
        var tasksPage = m_taskServiceHelper.findAllTasksByFilter(spec, page);

        // Convert tasks to TaskDTOs
        var tasks = tasksPage.getContent().stream().map(this::toTaskDTO).sorted(TaskDTO::compareTo).toList();

        return toMultipleResponseMessagePageable(tasksPage.getTotalPages(), page, tasksPage.getNumberOfElements(), tasks);
    }

    /**
     * Finds a task with the provided id.
     *
     * @param taskId The id of the task to find.
     * @return A ResponseMessage containing the result of the operation.
     */
    public ResponseMessage<?> findTaskByIdCallback(UUID taskId)
    {
        // Find task by id
        var task = findTaskByIdIfExist(taskId);
        // Convert assignees to UserDTOs
        var assignees = task.getAssignees().stream().map(m_userMapper::toUserDTO).toList();
        // Convert task to TaskDTO
        var dto = m_taskMapper.toTaskDTO(task, m_projectMapper.toProjectDTO(task.getProject()), assignees);

        return new ResponseMessage<>("Task found successfully", Status.OK, dto);
    }
//------------------------------------------------------------------------------------------------------------------

    /**
     * Finds a project with the provided id if it exists.
     *
     * @param projectId The id of the project to find.
     * @return The project with the provided id if it exists.
     */
    private Project findProjectByIdIfExist(UUID projectId)
    {
        ISupplier<Optional<Project>> projectSupplier = () -> m_taskServiceHelper.findProjectById(projectId);
        var project = doForDataService(projectSupplier, "TaskService::findProjectByIdIfExist");
        return project.orElseThrow(() -> new DataServiceException("Project not found"));
    }

    /**
     * Finds a task with the provided id if it exists.
     *
     * @param taskId The id of the task to find.
     * @return The task with the provided id if it exists.
     */
    private Task findTaskByIdIfExist(UUID taskId)
    {
        ISupplier<Optional<Task>> taskSupplier = () -> m_taskServiceHelper.findTaskById(taskId);
        var task = doForDataService(taskSupplier, "TaskService::findTaskByIdIfExist");
        return task.orElseThrow(() -> new DataServiceException("Task not found"));
    }


    /**
     * Sends a notification to a user.
     *
     * @param project The project the notification is related to.
     * @param user    The user to send the notification to.
     * @param owner   The owner of the project.
     * @param message The message to send in the notification.
     */
    private void sendNotificationToUser(Project project, User user, User owner, String message)
    {
        var data = new NotificationObject(project.getProjectId(), user.getUserId());
        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

        var notificationMessage = new NotificationKafkaDTO.Builder()
                .setFromUserId(owner.getUserId())
                .setToUserId(user.getUserId())
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .build();
        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendNotification(notificationMessage), "ProjectService::approveParticipantRequest");
    }

    /**
     * Converts a Task object to a TaskDTO object.
     *
     * @param task The Task object to convert.
     * @return The converted TaskDTO object.
     */
    public TaskDTO toTaskDTO(Task task)
    {
        var userDtoList = task.getAssignees().stream().map(m_userMapper::toUserDTO).toList();
        var projectDTO = m_projectMapper.toProjectDTO(task.getProject());
        return m_taskMapper.toTaskDTO(task, projectDTO, userDtoList);
    }

    /**
     * Sends a notification to the assignees of a task.
     *
     * @param createTaskDTO The task to send the notification for.
     * @param message       The message to send in the notification.
     */
    public void sendNotification(Task createTaskDTO, String message)
    {
        createTaskDTO.getAssignees().forEach(usr -> sendNotificationToUser(createTaskDTO.getProject(), usr, createTaskDTO.getProject().getProjectOwner(), message));
    }

    /**
     * Sends a notification to the assignees of a task.
     *
     * @param createTaskDTO The task to send the notification for.
     */
    public void sendNotification(Task createTaskDTO)
    {
        var participants = createTaskDTO.getAssignees();
        var stringBuilder = new StringBuilder();

        stringBuilder.append("A new task has been assigned to the ")
                .append(createTaskDTO.getProject().getProjectName())
                .append(" project. The deadline is ")
                .append(ofPattern("dd/MM/yyyy").format(createTaskDTO.getEndDate()))
                .append(".");

        for (var participant : participants)
        {
            var user = m_taskServiceHelper.findUserById(participant.getUserId());
            sendNotificationToUser(createTaskDTO.getProject(), user.get(), createTaskDTO.getProject().getProjectOwner(), stringBuilder.toString());
        }
    }

    /**
     * Converts a list of TaskDTOs to a MultipleResponseMessagePageable.
     *
     * @param totalPage   The total number of pages.
     * @param currentPage The current page number.
     * @param itemCount   The total number of items.
     * @param list        The list of TaskDTOs to convert.
     * @return A MultipleResponseMessagePageable containing the converted list of TaskDTOs.
     */
    public MultipleResponseMessagePageable<?> toMultipleResponseMessagePageable(int totalPage, int currentPage, int itemCount, List<TaskDTO> list)
    {
        return new MultipleResponseMessagePageable<>(totalPage, currentPage, itemCount, "Found " + itemCount + " tasks", list);
    }
}
