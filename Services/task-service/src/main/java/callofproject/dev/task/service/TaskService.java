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
import callofproject.dev.task.dto.request.*;
import callofproject.dev.task.dto.response.TaskDTO;
import callofproject.dev.task.mapper.IProjectMapper;
import callofproject.dev.task.mapper.ITaskMapper;
import callofproject.dev.task.mapper.IUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class TaskService
{
    private final TaskServiceHelper m_taskServiceHelper;
    private final KafkaProducer m_kafkaProducer;
    private final ObjectMapper m_objectMapper;
    private final ITaskMapper m_taskMapper;
    private final IProjectMapper m_projectMapper;
    private final IUserMapper m_userMapper;

    public TaskService(TaskServiceHelper taskServiceHelper, KafkaProducer kafkaProducer,
                       IProjectMapper projectMapper, ITaskMapper taskMapper,
                       ObjectMapper objectMapper, IUserMapper userMapper)
    {
        m_taskServiceHelper = taskServiceHelper;
        m_taskMapper = taskMapper;
        m_projectMapper = projectMapper;
        m_kafkaProducer = kafkaProducer;
        m_objectMapper = objectMapper;
        m_userMapper = userMapper;
    }

    public ResponseMessage<Object> createTask(CreateTaskDTO createTaskDTO)
    {
        var createTaskCallback = createTaskCallback(createTaskDTO);
        var task = (Task) createTaskCallback.getObject();

        if (createTaskCallback.getStatusCode() == Status.CREATED)
            sendNotification(task);

        createTaskCallback.setObject(toTaskDTO(task));
        return createTaskCallback;
    }

    public ResponseMessage<?> changeTaskStatus(ChangeTaskStatusDTO taskStatusDTO)
    {
        var changeTaskStatusCallback = changeTaskStatusCallback(taskStatusDTO);

        if (changeTaskStatusCallback.getStatusCode() == Status.OK)
            sendNotification((Task) changeTaskStatusCallback.getObject());

        return changeTaskStatusCallback;
    }

    public ResponseMessage<?> changeTaskPriority(ChangeTaskPriorityDTO priorityDTO)
    {
        var changeTaskPriorityCallback = changeTaskPriorityCallback(priorityDTO);

        if (changeTaskPriorityCallback.getStatusCode() == Status.OK)
            sendNotification((Task) changeTaskPriorityCallback.getObject());

        return changeTaskPriorityCallback;
    }

    public ResponseMessage<?> deleteTask(UUID taskId)
    {
        var removedTaskCallback = deleteTaskCallback(taskId);
        if (removedTaskCallback.getStatusCode() == Status.OK)
            sendNotification((Task) removedTaskCallback.getObject());
        removedTaskCallback.setObject("Task deleted successfully");
        return removedTaskCallback;
    }

    public ResponseMessage<?> updateTask(UpdateTaskDTO updateTaskDTO)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ResponseMessage<?> deleteTaskByTaskIdAndUserId(UUID userId, UUID taskId)
    {
        var removedTaskCallback = doForDataService(() -> deleteTaskByTaskIdAndUserIdCallback(taskId, userId),
                "TaskService::deleteTaskByTaskIdAndUserId");

        var pair = (Pair<User, Task>) removedTaskCallback.getObject();
        var task = pair.getSecond();
        var message = "You have been removed from the " + task.getTitle() + " task in the " + task.getProject().getProjectName() + " project.";

        if (removedTaskCallback.getStatusCode() == Status.OK)
            sendNotificationToUser(task.getProject(), pair.getFirst(), task.getProject().getProjectOwner(), message);

        return new ResponseMessage<>("You removed from task successfully!", Status.OK, "You removed from task successfully!");
    }


    public MultipleResponseMessagePageable<?> findTasksByProjectId(UUID projectId, int page)
    {
        var taskPage = m_taskServiceHelper.findAllTasksByProjectId(projectId, page);

        var tasks = doForDataService(() -> taskPage.getContent().stream()
                .map(this::toTaskDTO)
                .toList(), "TaskService::findTasksByProjectId");

        return toMultipleResponseMessagePageable(taskPage.getTotalPages(), page, taskPage.getNumberOfElements(), tasks);
    }

    public MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserId(UUID projectId, UUID userId, int page)
    {
        var taskPage = m_taskServiceHelper.findAllTasksByProjectId(projectId, page);

        var tasks = doForDataService(() -> taskPage.getContent().stream()
                .filter(t -> t.getAssignees().stream().anyMatch(ta -> ta.getUserId().equals(userId)))
                .map(this::toTaskDTO)
                .toList(), "TaskService::findTasksByProjectIdAndUserId");

        return toMultipleResponseMessagePageable(taskPage.getTotalPages(), page, taskPage.getNumberOfElements(), tasks);
    }

    public ResponseMessage<?> findTaskById(UUID taskId)
    {
        var task = findTaskByIdIfExist(taskId);

        return new ResponseMessage<>("Task found successfully", Status.OK, task);
    }
    //------------------------------------------------------------------------------------------------------------------

    private MultipleResponseMessagePageable<?> toMultipleResponseMessagePageable(int totalPage, int currentPage, int itemCount, List<?> list)
    {
        var msg = "Found " + itemCount + " tasks";
        return new MultipleResponseMessagePageable<>(totalPage, currentPage, itemCount, msg, list);
    }

    public ResponseMessage<?> deleteTaskByTaskIdAndUserIdCallback(UUID taskId, UUID userId)
    {
        var task = findTaskByIdIfExist(taskId);
        var taskAssignees = task.getAssignees();
        var user = taskAssignees.stream().filter(u -> u.getUserId().equals(userId)).findFirst();

        if (user.isEmpty())
            throw new DataServiceException("User not found");

        task.getAssignees().remove(user.get());

        doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::deleteTaskCallback");

        return new ResponseMessage<>("You removed from task successfully!", Status.OK, new Pair<>(user.get(), task));
    }

    public ResponseMessage<Object> deleteTaskCallback(UUID taskId)
    {
        var task = findTaskByIdIfExist(taskId);
        var project = task.getProject();
        var taskAssignees = task.getAssignees();

        if (taskAssignees != null && !taskAssignees.isEmpty())
            taskAssignees.forEach(user -> user.getAssignedTasks().remove(task));

        doForDataService(() -> m_taskServiceHelper.deleteTask(task), "TaskService::deleteTaskCallback");
        var message = "You have been removed from the " + task.getTitle() + " task in the " + project.getProjectName() + " project.";

        for (var user : taskAssignees)
            sendNotificationToUser(project, user, project.getProjectOwner(), message);

        return new ResponseMessage<>("Task deleted successfully", Status.OK, task);
    }

    public ResponseMessage<?> changeTaskPriorityCallback(ChangeTaskPriorityDTO dto)
    {
        var task = findTaskByIdIfExist(dto.taskId());

        if (task.getProject().getProjectOwner().getUserId().equals(dto.userId()))
            throw new DataServiceException("You are not the owner of this project");

        task.setPriority(dto.priority());

        doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::changeTaskPriorityCallback");

        return new ResponseMessage<>("Task priority changed successfully", Status.OK, "Task priority changed successfully");
    }

    public ResponseMessage<?> changeTaskStatusCallback(ChangeTaskStatusDTO dto)
    {
        var task = findTaskByIdIfExist(dto.taskId());

        if (task.getProject().getProjectOwner().getUserId().equals(dto.userId()))
            throw new DataServiceException("You are not the owner of this project");

        task.setTaskStatus(dto.status());

        doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::changeTaskStatusCallback");

        return new ResponseMessage<>("Task status changed successfully", Status.OK, "Task status changed successfully");
    }

    public MultipleResponseMessagePageable<?> findTaskByFilter(TaskFilterDTO dto, int page)
    {
        var spec = Specification.where(TaskFilterSpecifications.hasPriority(dto.priority()))
                .and(hasTaskStatus(dto.taskStatus()))
                .and(hasStartDate(dto.startDate()))
                .and(hasFinishDate(dto.finishDate()))
                .and(hasProjectId(dto.projectId()))
                .and(hasProjectOwnerId(dto.projectOwnerId()));

        System.out.println(spec.toString());
        var tasksPage = m_taskServiceHelper.findAllTasksByFilter(spec, page);

        var tasks = doForDataService(() -> tasksPage.getContent().stream()
                .map(this::toTaskDTO)
                .toList(), "TaskService::findTaskByFilter");

        return toMultipleResponseMessagePageable(tasksPage.getTotalPages(), page, tasksPage.getNumberOfElements(), tasks);
    }

    public ResponseMessage<Object> createTaskCallback(CreateTaskDTO createTaskDTO)
    {
        if (createTaskDTO.startDate().isAfter(createTaskDTO.endDate()))
            throw new DataServiceException("Start date cannot be after end date");

        var project = findProjectByIdIfExist(createTaskDTO.projectId());

        var users = toStream(m_taskServiceHelper.findUsersByIds(createTaskDTO.userIds())).collect(toSet());

        ISupplier<Task> taskSupplier = () -> m_taskServiceHelper.saveTask(m_taskMapper.toTask(createTaskDTO, project, users));

        var savedTask = doForDataService(taskSupplier, "TaskService::createTaskCallback");

        return new ResponseMessage<>("Task created successfully", Status.CREATED, savedTask);
    }
//------------------------------------------------------------------------------------------------------------------

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

    private TaskDTO toTaskDTO(Task task)
    {
        var userDtoList = task.getAssignees().stream().map(m_userMapper::toUserDTO).toList();
        var projectDTO = m_projectMapper.toProjectDTO(task.getProject());
        return m_taskMapper.toTaskDTO(task, projectDTO, userDtoList);
    }

    private void sendNotification(Task createTaskDTO)
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


    private Project findProjectByIdIfExist(UUID projectId)
    {
        ISupplier<Optional<Project>> projectSupplier = () -> m_taskServiceHelper.findProjectById(projectId);
        var project = doForDataService(projectSupplier, "TaskService::findProjectByIdIfExist");
        return project.orElseThrow(() -> new DataServiceException("Project not found"));
    }

    private Task findTaskByIdIfExist(UUID taskId)
    {
        ISupplier<Optional<Task>> taskSupplier = () -> m_taskServiceHelper.findTaskById(taskId);
        var task = doForDataService(taskSupplier, "TaskService::findTaskByIdIfExist");
        return task.orElseThrow(() -> new DataServiceException("Task not found"));
    }

}
