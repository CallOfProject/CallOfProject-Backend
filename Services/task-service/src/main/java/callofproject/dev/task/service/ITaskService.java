package callofproject.dev.task.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.task.dto.UserDTO;
import callofproject.dev.task.dto.request.*;

import java.util.List;
import java.util.UUID;

public interface ITaskService
{
    ResponseMessage<Object> createTask(CreateTaskDTO createTaskDTO);

    ResponseMessage<?> changeTaskStatus(ChangeTaskStatusDTO taskStatusDTO);

    ResponseMessage<?> deleteTask(UUID taskId);

    ResponseMessage<?> deleteTaskByTaskIdAndUserId(UUID userId, UUID taskId);

    ResponseMessage<?> updateTask(UpdateTaskDTO updateTaskDTO);

    ResponseMessage<?> changeTaskPriority(ChangeTaskPriorityDTO priorityDTO);

    ResponseMessage<?> findTaskById(UUID taskId);

    MultipleResponseMessagePageable<?> findTasksByProjectId(UUID projectId, int page);

    MultipleResponseMessagePageable<?> findTasksByProjectIdAndUserId(UUID projectId, UUID userId, int page);

    MultipleResponseMessagePageable<?> findTaskByFilter(TaskFilterDTO dto, int page);

    List<UserDTO> findAllAssigneesByTaskId(UUID userId, UUID taskId);
}
