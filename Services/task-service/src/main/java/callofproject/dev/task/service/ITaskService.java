package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.*;

import java.util.List;
import java.util.UUID;

/**
 * Task service interface
 * <p>
 * This interface contains methods for task service
 */
public interface ITaskService
{
    /**
     * Create a new task
     *
     * @param createTaskDTO create task dto
     * @return ResponseMessage
     */
    ResponseMessage<Object> createTask(CreateTaskDTO createTaskDTO);

    /**
     * Change task status
     *
     * @param taskStatusDTO task status dto
     * @return ResponseMessage
     */
    ResponseMessage<?> changeTaskStatus(ChangeTaskStatusDTO taskStatusDTO);

    /**
     * Delete task
     *
     * @param taskId task id
     * @return ResponseMessage
     */
    ResponseMessage<?> deleteTask(UUID taskId);

    /**
     * Delete task by task id and user id
     *
     * @param userId user id
     * @param taskId task id
     * @return ResponseMessage
     */
    ResponseMessage<?> deleteTaskByTaskIdAndUserId(UUID userId, UUID taskId);

    /**
     * Update task
     *
     * @param updateTaskDTO update task dto
     * @return ResponseMessage
     */
    ResponseMessage<?> updateTask(UpdateTaskDTO updateTaskDTO);

    /**
     * Change task priority
     *
     * @param priorityDTO priority dto
     * @return ResponseMessage
     */
    ResponseMessage<?> changeTaskPriority(ChangeTaskPriorityDTO priorityDTO);

    /**
     * Find task by id
     *
     * @param taskId task id
     * @return ResponseMessage
     */
    ResponseMessage<?> findTaskById(UUID taskId);

    /**
     * Find tasks by project id
     *
     * @param projectId project id
     * @param page      page
     * @return ResponseMessage
     */
    MultipleResponseMessagePageable<?> findTasksByProjectId(UUID projectId, int page);

    /**
     * Find tasks by project id and user id
     *
     * @param projectId project id
     * @param userId    user id
     * @param page      page
     * @return ResponseMessage
     */
    MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserId(UUID projectId, UUID userId, int page);

    /**
     * Find task by filter
     *
     * @param dto  task filter dto
     * @param page page
     * @return ResponseMessage
     */
    MultipleResponseMessagePageable<?> findTaskByFilter(TaskFilterDTO dto, int page);

    /**
     * Find all assignees by task id
     *
     * @param userId user id
     * @param taskId task id
     * @return List of UserDTO
     */
    List<UserDTO> findAllAssigneesByTaskId(UUID userId, UUID taskId);
}