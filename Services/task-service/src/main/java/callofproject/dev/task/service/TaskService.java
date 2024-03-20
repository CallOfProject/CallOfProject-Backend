package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.dsa.Pair;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.*;
import callofproject.dev.task.dto.response.TaskDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * The type Task service.
 * This class is used to handle the task service.
 */
@Service
@Lazy
@SuppressWarnings("all")
public class TaskService implements ITaskService
{
    private final TaskServiceCallback m_taskServiceCallback;

    /**
     * Instantiates a new Task service.
     *
     * @param taskServiceCallback the task service callback
     */
    public TaskService(TaskServiceCallback taskServiceCallback)
    {
        m_taskServiceCallback = taskServiceCallback;
    }


    /**
     * Create a new task
     *
     * @param createTaskDTO the create task dto
     * @return the response message
     */
    @Override
    public ResponseMessage<Object> createTask(CreateTaskDTO createTaskDTO)
    {
        ISupplier<ResponseMessage<Object>> callback = () -> m_taskServiceCallback.createTaskCallback(createTaskDTO);
        var createTaskCallback = doForDataService(callback::get, "TaskService::CreateTask");

        var task = (Task) createTaskCallback.getObject();

        if (createTaskCallback.getStatusCode() == Status.CREATED)
            m_taskServiceCallback.sendNotification(task, getCreatedMessage(task));

        createTaskCallback.setObject(m_taskServiceCallback.toTaskDTO(task));

        return createTaskCallback;
    }

    /**
     * Change task status
     *
     * @param taskStatusDTO the task status dto
     * @return the response message
     */
    @Override
    public ResponseMessage<?> changeTaskStatus(ChangeTaskStatusDTO taskStatusDTO)
    {
        ISupplier<ResponseMessage<Object>> callback = () -> m_taskServiceCallback.changeTaskStatusCallback(taskStatusDTO);
        var changeTaskStatusCallback = doForDataService(callback::get, "TaskService::changeTaskStatus");

        var pair = (Pair<String, Task>) changeTaskStatusCallback.getObject();

        if (changeTaskStatusCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(pair.getSecond());

        changeTaskStatusCallback.setObject(pair.getFirst());

        return changeTaskStatusCallback;
    }

    /**
     * Delete task
     *
     * @param taskId the task id
     * @return the response message
     */
    @Override
    public ResponseMessage<?> deleteTask(UUID taskId)
    {
        ISupplier<ResponseMessage<Object>> callback = () -> m_taskServiceCallback.deleteTaskCallback(taskId);
        var removedTaskCallback = doForDataService(callback::get, "TaskService::deleteTask");

        var task = (Task) removedTaskCallback.getObject();

        if (removedTaskCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(task, getRemovedMessage(task));

        removedTaskCallback.setObject("Task deleted successfully");

        return removedTaskCallback;
    }

    /**
     * Delete task by task id and user id
     *
     * @param userId the user id
     * @param taskId the task id
     * @return the response message
     */
    @Override
    public ResponseMessage<?> deleteTaskByTaskIdAndUserId(UUID userId, UUID taskId)
    {
        ISupplier<ResponseMessage<?>> callback = () -> m_taskServiceCallback.deleteTaskByTaskIdAndUserIdCallback(taskId, userId);

        var removedTaskCallback = doForDataService(callback::get, "TaskService::deleteTaskByTaskIdAndUserId");

        var task = (Task) removedTaskCallback.getObject();

        if (removedTaskCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(task, getRemovedMessage(task));

        return new ResponseMessage<>("You removed from task successfully!", Status.OK, "You removed from task successfully!");
    }

    /**
     * Update task
     *
     * @param updateTaskDTO the update task dto
     * @return the response message
     */
    @Override
    public ResponseMessage<?> updateTask(UpdateTaskDTO updateTaskDTO)
    {
        ISupplier<ResponseMessage<Object>> callback = () -> m_taskServiceCallback.updateTaskCallback(updateTaskDTO);
        var updateCallback = doForDataService(callback::get, "TaskService::updateTask");
        var pair = (Pair<Task, TaskDTO>) updateCallback.getObject();

        if (updateCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(pair.getFirst());

        updateCallback.setObject(pair.getSecond());
        return updateCallback;
    }

    /**
     * Change task priority
     *
     * @param priorityDTO the priority dto
     * @return the response message
     */
    @Override
    public ResponseMessage<?> changeTaskPriority(ChangeTaskPriorityDTO priorityDTO)
    {
        ISupplier<ResponseMessage<Object>> callback = () -> m_taskServiceCallback.changeTaskPriorityCallback(priorityDTO);
        var changeTaskPriorityCallback = doForDataService(callback::get, "TaskService::changeTaskPriority");
        var pair = (Pair<String, Task>) changeTaskPriorityCallback.getObject();

        if (changeTaskPriorityCallback.getStatusCode() == Status.OK)
            m_taskServiceCallback.sendNotification(pair.getSecond());

        changeTaskPriorityCallback.setObject(pair.getFirst());

        return changeTaskPriorityCallback;
    }

    /**
     * Find task by id
     *
     * @param taskId the task id
     * @return the response message
     */
    @Override
    public List<UserDTO> findAllAssigneesByTaskId(UUID userId, UUID taskId)
    {
        return doForDataService(() -> m_taskServiceCallback.findAllAssigneesByTaskIdCallback(userId, taskId), "TaskService::findAllAssigneesByTaskId");
    }

    /**
     * Find tasks by project id
     *
     * @param projectId the project id
     * @param page      the page
     * @return the response message
     */
    @Override
    public MultipleResponseMessagePageable<?> findTasksByProjectId(UUID projectId, int page)
    {
        return doForDataService(() -> m_taskServiceCallback.findTasksByProjectIdCallback(projectId, page), "TaskService::findTasksByProjectId");
    }

    /**
     * Find tasks by project id and user id
     *
     * @param projectId the project id
     * @param userId    the user id
     * @param page      the page
     * @return the response message
     */
    @Override
    public MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserId(UUID projectId, UUID userId, int page)
    {
        return doForDataService(() -> m_taskServiceCallback.findTasksByProjectIdAndUserIdCallback(projectId, userId, page), "TaskService::findTasksByProjectIdAndUserId");
    }

    /**
     * Find task by filter
     *
     * @param dto  the dto
     * @param page the page
     * @return the response message
     */
    @Override
    public MultipleResponseMessagePageable<?> findTaskByFilter(TaskFilterDTO dto, int page)
    {
        return doForDataService(() -> m_taskServiceCallback.findTaskByFilterCallback(dto, page), "TaskService::findTaskByFilter");
    }

    /**
     * Find all assignees by task id
     *
     * @param taskId the task id
     * @return the response message
     */
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
