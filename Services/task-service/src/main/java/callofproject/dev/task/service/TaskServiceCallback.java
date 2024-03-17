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

    public TaskServiceCallback(TaskServiceHelper taskServiceHelper, KafkaProducer kafkaProducer, ObjectMapper objectMapper, ITaskMapper taskMapper, IProjectMapper projectMapper, IUserMapper userMapper)
    {
        m_taskServiceHelper = taskServiceHelper;
        m_kafkaProducer = kafkaProducer;
        m_objectMapper = objectMapper;
        m_taskMapper = taskMapper;
        m_projectMapper = projectMapper;
        m_userMapper = userMapper;
    }


    public ResponseMessage<Object> updateTaskCallback(UpdateTaskDTO updateTaskDTO)
    {
        var task = findTaskByIdIfExist(updateTaskDTO.taskId());

        if (updateTaskDTO.startDate().isAfter(updateTaskDTO.endDate()))
            throw new DataServiceException("Start date cannot be after end date");

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

    public ResponseMessage<?> deleteTaskByTaskIdAndUserIdCallback(UUID taskId, UUID userId)
    {
        var task = findTaskByIdIfExist(taskId);
        var taskAssignees = task.getAssignees();
        var user = taskAssignees.stream().filter(u -> u.getUserId().equals(userId)).findFirst();

        if (user.isEmpty())
            throw new DataServiceException("User not found");

        task.getAssignees().remove(user.get());

        var savedTask = m_taskServiceHelper.saveTask(task);

        if (savedTask.getAssignees().isEmpty())
            m_taskServiceHelper.deleteTask(savedTask);

        return new ResponseMessage<>("You removed from task successfully!", Status.OK, task);
    }


    public ResponseMessage<Object> deleteTaskCallback(UUID taskId)
    {
        var task = findTaskByIdIfExist(taskId);
        var taskAssignees = task.getAssignees();

        if (taskAssignees != null && !taskAssignees.isEmpty())
            taskAssignees.forEach(user -> user.getAssignedTasks().remove(task));

        m_taskServiceHelper.deleteTask(task);

        return new ResponseMessage<>("Task deleted successfully", Status.OK, task);
    }

    public ResponseMessage<Object> changeTaskPriorityCallback(ChangeTaskPriorityDTO dto)
    {
        var task = findTaskByIdIfExist(dto.taskId());

        if (task.getProject().getProjectOwner().getUserId().equals(dto.userId()))
            throw new DataServiceException("You are not the owner of this project");

        task.setPriority(dto.priority());

        doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::changeTaskPriorityCallback");

        return new ResponseMessage<>("Task priority changed successfully", Status.OK, new Pair<>("Task priority changed successfully", task));
    }

    public ResponseMessage<Object> changeTaskStatusCallback(ChangeTaskStatusDTO dto)
    {
        var task = findTaskByIdIfExist(dto.taskId());

        if (task.getProject().getProjectOwner().getUserId().equals(dto.userId()))
            throw new DataServiceException("You are not the owner of this project");

        task.setTaskStatus(dto.status());

        doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::changeTaskStatusCallback");

        return new ResponseMessage<>("Task status changed successfully", Status.OK, new Pair<>("Task status changed successfully", task));
    }

    public MultipleResponseMessagePageable<?> findTaskByFilterCallback(TaskFilterDTO dto, int page)
    {
        var spec = Specification.where(TaskFilterSpecifications.hasPriority(dto.priority()))
                .and(hasTaskStatus(dto.taskStatus()))
                .and(hasStartDate(dto.startDate()))
                .and(hasFinishDate(dto.finishDate()))
                .and(hasProjectId(dto.projectId()))
                .and(hasProjectOwnerId(dto.projectOwnerId()));

        var tasksPage = m_taskServiceHelper.findAllTasksByFilter(spec, page);

        var tasks = tasksPage.getContent().stream().map(this::toTaskDTO).sorted(TaskDTO::compareTo).toList();

        return toMultipleResponseMessagePageable(tasksPage.getTotalPages(), page, tasksPage.getNumberOfElements(), tasks);
    }

    public ResponseMessage<Object> createTaskCallback(CreateTaskDTO createTaskDTO)
    {
        if (createTaskDTO.startDate().isAfter(createTaskDTO.endDate()))
            throw new DataServiceException("Start date cannot be after end date");

        var project = findProjectByIdIfExist(createTaskDTO.projectId());
        var users = toStream(m_taskServiceHelper.findUsersByIds(createTaskDTO.userIds())).collect(toSet());
        var savedTask = m_taskServiceHelper.saveTask(m_taskMapper.toTask(createTaskDTO, project, users));

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

    public TaskDTO toTaskDTO(Task task)
    {
        var userDtoList = task.getAssignees().stream().map(m_userMapper::toUserDTO).toList();
        var projectDTO = m_projectMapper.toProjectDTO(task.getProject());
        return m_taskMapper.toTaskDTO(task, projectDTO, userDtoList);
    }

    public void sendNotification(Task createTaskDTO, String message)
    {
        //System.out.println(createTaskDTO.getAssignees().size());
        createTaskDTO.getAssignees().forEach(usr -> sendNotificationToUser(createTaskDTO.getProject(), usr, createTaskDTO.getProject().getProjectOwner(), message));
    }

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

    public List<UserDTO> findAllAssigneesByTaskIdCallback(UUID userId, UUID taskId)
    {
        var task = findTaskByIdIfExist(taskId);

        if (!task.getProject().getProjectOwner().getUserId().equals(userId))
            throw new DataServiceException("You are not the owner of this project");

        return doForDataService(() -> task.getAssignees().stream().map(m_userMapper::toUserDTO).toList(), "TaskService::findAllAssigneesByTaskId");
    }


    public MultipleResponseMessagePageable<?> toMultipleResponseMessagePageable(int totalPage, int currentPage, int itemCount, List<TaskDTO> list)
    {
        var msg = "Found " + itemCount + " tasks";
        return new MultipleResponseMessagePageable<>(totalPage, currentPage, itemCount, msg, list);
    }


    public MultipleResponseMessagePageable<?> findTasksByProjectIdCallback(UUID projectId, int page)
    {
        var taskPage = m_taskServiceHelper.findAllTasksByProjectId(projectId, page);

        var tasks = doForDataService(() -> taskPage.getContent().stream()
                .map(this::toTaskDTO)
                .sorted(TaskDTO::compareTo)
                .toList(), "TaskService::findTasksByProjectId");

        return toMultipleResponseMessagePageable(taskPage.getTotalPages(), page, taskPage.getNumberOfElements(), tasks);
    }

    public MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserIdCallback(UUID projectId, UUID userId, int page)
    {
        var taskPage = m_taskServiceHelper.findAllTasksByProjectId(projectId, page);

        var tasks = doForDataService(() -> taskPage.getContent().stream()
                .filter(t -> t.getAssignees().stream().anyMatch(ta -> ta.getUserId().equals(userId)))
                .map(this::toTaskDTO)
                .sorted(TaskDTO::compareTo)
                .toList(), "TaskService::findTasksByProjectIdAndUserId");

        return toMultipleResponseMessagePageable(taskPage.getTotalPages(), page, taskPage.getNumberOfElements(), tasks);
    }


    public ResponseMessage<?> findTaskByIdCallback(UUID taskId)
    {
        var task = findTaskByIdIfExist(taskId);

        var dto = m_taskMapper.toTaskDTO(task, m_projectMapper.toProjectDTO(task.getProject()), task.getAssignees().stream().map(m_userMapper::toUserDTO).toList());

        return new ResponseMessage<>("Task found successfully", Status.OK, dto);
    }
}