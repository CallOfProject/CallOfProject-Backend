package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dsa.Pair;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.*;
import callofproject.dev.task.dto.response.TaskDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.time.format.DateTimeFormatter.ofPattern;

@Service
@Lazy
@SuppressWarnings("all")
public class TaskService implements ITaskService
{
    private final TaskServiceCallback m_taskServiceCallback;

    public TaskService(TaskServiceCallback taskServiceCallback)
    {
        m_taskServiceCallback = taskServiceCallback;
    }


    @Override
    public ResponseMessage<Object> createTask(CreateTaskDTO createTaskDTO)
    {
        var createTaskCallback = doForDataService(() -> m_taskServiceCallback.createTaskCallback(createTaskDTO), "TaskService::CreateTask");
        var task = (Task) createTaskCallback.getObject();

        if (createTaskCallback.getStatusCode() == Status.CREATED)
            m_taskServiceCallback.sendNotification(task, getCreatedMessage(task));

        createTaskCallback.setObject(m_taskServiceCallback.toTaskDTO(task));

        return createTaskCallback;
    }

    @Override
    public ResponseMessage<?> changeTaskStatus(ChangeTaskStatusDTO taskStatusDTO)
    {
        var changeTaskStatusCallback = m_taskServiceCallback.changeTaskStatusCallback(taskStatusDTO);
        var pair = (Pair<String, Task>) changeTaskStatusCallback.getObject();
        if (changeTaskStatusCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(pair.getSecond());
        changeTaskStatusCallback.setObject(pair.getFirst());
        return changeTaskStatusCallback;
    }

    @Override
    public ResponseMessage<?> deleteTask(UUID taskId)
    {
        var removedTaskCallback = doForDataService(() -> m_taskServiceCallback.deleteTaskCallback(taskId), "TaskService::deleteTask");

        var task = (Task) removedTaskCallback.getObject();

        if (removedTaskCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(task, getRemovedMessage(task));

        removedTaskCallback.setObject("Task deleted successfully");

        return removedTaskCallback;
    }

    @Override
    public ResponseMessage<?> deleteTaskByTaskIdAndUserId(UUID userId, UUID taskId)
    {
        var removedTaskCallback = doForDataService(() -> m_taskServiceCallback.deleteTaskByTaskIdAndUserIdCallback(taskId, userId),
                "TaskService::deleteTaskByTaskIdAndUserId");

        var task = (Task) removedTaskCallback.getObject();

        if (removedTaskCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(task, getRemovedMessage(task));

        return new ResponseMessage<>("You removed from task successfully!", Status.OK, "You removed from task successfully!");
    }

    @Override
    public ResponseMessage<?> updateTask(UpdateTaskDTO updateTaskDTO)
    {
        var updateCallback = m_taskServiceCallback.updateTaskCallback(updateTaskDTO);
        var pair = (Pair<Task, TaskDTO>) updateCallback.getObject();

        if (updateCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(pair.getFirst());

        updateCallback.setObject(pair.getSecond());
        return updateCallback;
    }

    @Override
    public ResponseMessage<?> changeTaskPriority(ChangeTaskPriorityDTO priorityDTO)
    {
        var changeTaskPriorityCallback = m_taskServiceCallback.changeTaskPriorityCallback(priorityDTO);

        var pair = (Pair<String, Task>) changeTaskPriorityCallback.getObject();

        if (changeTaskPriorityCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(pair.getSecond());

        changeTaskPriorityCallback.setObject(pair.getFirst());

        return changeTaskPriorityCallback;
    }

    @Override
    public List<UserDTO> findAllAssigneesByTaskId(UUID userId, UUID taskId)
    {
        return doForDataService(() -> m_taskServiceCallback.findAllAssigneesByTaskIdCallback(userId, taskId), "TaskService::findAllAssigneesByTaskId");
    }

    @Override
    public MultipleResponseMessagePageable<?> findTasksByProjectId(UUID projectId, int page)
    {
        return doForDataService(() -> m_taskServiceCallback.findTasksByProjectIdCallback(projectId, page), "TaskService::findTasksByProjectId");
    }

    @Override
    public MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserId(UUID projectId, UUID userId, int page)
    {
        return doForDataService(() -> m_taskServiceCallback.findTasksByProjectIdAndUserIdCallback(projectId, userId, page), "TaskService::findTasksByProjectIdAndUserId");
    }

    @Override
    public MultipleResponseMessagePageable<?> findTaskByFilter(TaskFilterDTO dto, int page)
    {
        return doForDataService(() -> m_taskServiceCallback.findTaskByFilterCallback(dto, page), "TaskService::findTaskByFilter");
    }

    @Override
    public ResponseMessage<?> findTaskById(UUID taskId)
    {
        return doForDataService(() -> m_taskServiceCallback.findTaskByIdCallback(taskId), "TaskService::findTaskById");
    }

    private String getCreatedMessage(Task task)
    {
        return "A new task has been assigned to the \"" + task.getProject().getProjectName() +
                "\" project. The deadline is " + ofPattern("dd/MM/yyyy").format(task.getEndDate()) + ".";
    }

    private String getRemovedMessage(Task task)
    {
        return "You have been removed from the \"" + task.getTitle() +
                "\" task in the \"" + task.getProject().getProjectName() + "\" project.";
    }
}
