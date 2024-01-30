package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.task.config.kafka.KafkaProducer;
import callofproject.dev.task.dto.NotificationKafkaDTO;
import callofproject.dev.task.dto.NotificationObject;
import callofproject.dev.task.dto.request.ChangeTaskPriorityDTO;
import callofproject.dev.task.dto.request.ChangeTaskStatusDTO;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import callofproject.dev.task.dto.request.UpdateTaskDTO;
import callofproject.dev.task.mapper.IProjectMapper;
import callofproject.dev.task.mapper.ITaskMapper;
import callofproject.dev.task.mapper.IUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
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

        return toTaskDTO(task, createTaskCallback);
    }

    public ResponseMessage<?> updateTask(UpdateTaskDTO updateTaskDTO)
    {
        throw new UnsupportedOperationException("Not implemented yet");
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
    //------------------------------------------------------------------------------------------------------------------

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

    private ResponseMessage<Object> toTaskDTO(Task task, ResponseMessage<Object> createTaskCallback)
    {
        var userDtoList = task.getAssignees().stream().map(m_userMapper::toUserDTO).toList();
        var projectDTO = m_projectMapper.toProjectDTO(task.getProject());
        var taskDTO = m_taskMapper.toTaskDTO(task, projectDTO, userDtoList);
        createTaskCallback.setObject(taskDTO);
        return createTaskCallback;
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
