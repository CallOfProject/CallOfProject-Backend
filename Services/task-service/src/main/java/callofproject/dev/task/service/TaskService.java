package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import callofproject.dev.task.mapper.ITaskMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.util.stream.Collectors.toSet;

@Service
public class TaskService
{
    private final TaskServiceHelper m_taskServiceHelper;
    private final ITaskMapper m_taskMapper;

    public TaskService(TaskServiceHelper taskServiceHelper, ITaskMapper taskMapper)
    {
        m_taskServiceHelper = taskServiceHelper;
        m_taskMapper = taskMapper;
    }

    public ResponseMessage<?> createTask(CreateTaskDTO createTaskDTO)
    {
        var createTaskCallback = createTaskCallback(createTaskDTO);

        if (createTaskCallback.getStatusCode() == Status.CREATED)
            sendNotification((Task) createTaskCallback.getObject());

        return createTaskCallback;
    }

    public ResponseMessage<?> completeTask(UUID userId, UUID taskId)
    {
        var completeTaskCallback = completeTaskCallback(userId, taskId);

        if (completeTaskCallback.getStatusCode() == Status.OK)
            sendNotification((Task) completeTaskCallback.getObject());

        return completeTaskCallback;
    }
    //------------------------------------------------------------------------------------------------------------------

    public ResponseMessage<?> completeTaskCallback(UUID userId, UUID taskId)
    {
        var task = findTaskByIdIfExist(taskId);

        if (task.getProject().getProjectOwner().getUserId().equals(userId))
            throw new DataServiceException("You are not the owner of this project");

        task.setTaskStatus(TaskStatus.COMPLETED);

        doForDataService(() -> m_taskServiceHelper.saveTask(task), "TaskService::completeTaskCallback");

        return new ResponseMessage<>("Task completed successfully", Status.OK, "Task completed successfully");
    }

    public ResponseMessage<?> createTaskCallback(CreateTaskDTO createTaskDTO)
    {
        var project = findProjectByIdIfExist(createTaskDTO.projectId());
        var users = toStream(m_taskServiceHelper.findUsersByIds(createTaskDTO.userIds())).collect(toSet());

        ISupplier<Task> taskSupplier = () -> m_taskServiceHelper.saveTask(m_taskMapper.toTask(createTaskDTO, project, users));

        var savedTask = doForDataService(taskSupplier, "TaskService::createTaskCallback");

        return new ResponseMessage<>("Task created successfully", Status.CREATED, savedTask);
    }
//------------------------------------------------------------------------------------------------------------------

    private void sendNotification(Task createTaskDTO)
    {

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
