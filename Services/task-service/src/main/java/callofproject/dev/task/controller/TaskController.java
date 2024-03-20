package callofproject.dev.task.controller;

import callofproject.dev.task.dto.request.*;
import callofproject.dev.task.service.ITaskService;
import callofproject.dev.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

/**
 * The type Task controller.
 */
@RestController
@RequestMapping("api/task")
public class TaskController
{
    private final ITaskService m_taskService;

    /**
     * Instantiates a new Task controller.
     *
     * @param taskService the task service
     */
    public TaskController(TaskService taskService)
    {
        m_taskService = taskService;
    }

    /**
     * Create task response entity.
     *
     * @param createTaskDTO the create task dto
     * @return the response entity
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO)
    {
        return subscribe(() -> ok(m_taskService.createTask(createTaskDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Update task response entity.
     *
     * @param updateTaskDTO the update task dto
     * @return the response entity
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody @Valid UpdateTaskDTO updateTaskDTO)
    {
        return subscribe(() -> ok(m_taskService.updateTask(updateTaskDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Delete task response entity.
     *
     * @param taskId the task id
     * @return the response entity
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestParam("tid") UUID taskId)
    {
        return subscribe(() -> ok(m_taskService.deleteTask(taskId)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Delete task response entity.
     *
     * @param taskId the task id
     * @param userId the user id
     * @return the response entity
     */
    @DeleteMapping("/delete/by/task-and-user")
    public ResponseEntity<?> deleteTask(@RequestParam("tid") UUID taskId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_taskService.deleteTaskByTaskIdAndUserId(userId, taskId)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Change task status response entity.
     *
     * @param taskStatusDTO the task status dto
     * @return the response entity
     */
    @PutMapping("/change/status")
    public ResponseEntity<?> changeTaskStatus(@RequestBody @Valid ChangeTaskStatusDTO taskStatusDTO)
    {
        return subscribe(() -> ok(m_taskService.changeTaskStatus(taskStatusDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Change task priority response entity.
     *
     * @param priorityDTO the priority dto
     * @return the response entity
     */
    @PutMapping("/change/priority")
    public ResponseEntity<?> changeTaskPriority(@RequestBody @Valid ChangeTaskPriorityDTO priorityDTO)
    {
        return subscribe(() -> ok(m_taskService.changeTaskPriority(priorityDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Find tasks by user id
     *
     * @param projectId the project id
     * @param page      the page
     * @return the response entity
     */
    @GetMapping("/find/by/project")
    public ResponseEntity<?> findTasksByProjectId(@RequestParam("pid") UUID projectId, @RequestParam(value = "p", defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_taskService.findTasksByProjectId(projectId, page)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Find tasks by project id and user id
     *
     * @param projectId the project id
     * @param userId    the user id
     * @param page      the page
     * @return the response entity
     */
    @GetMapping("/find/by/project-and-user")
    public ResponseEntity<?> findTasksByProjectIdAndUserId(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId,
                                                           @RequestParam(value = "p", defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_taskService.findTasksByProjectIdAndUserId(projectId, userId, page)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Find task by id
     *
     * @param taskId the task id
     * @return the response entity
     */
    @GetMapping("/find/by/id")
    public ResponseEntity<?> findTaskById(@RequestParam("tid") UUID taskId)
    {
        return subscribe(() -> ok(m_taskService.findTaskById(taskId)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Find task by filter
     *
     * @param dto  the dto
     * @param page the page
     * @return the response entity
     */
    @PostMapping("/find/by/filter")
    public ResponseEntity<?> findTaskByFilter(@RequestBody TaskFilterDTO dto, @RequestParam(value = "p", defaultValue = "1") int page)
    {
        return subscribe(() -> ok(m_taskService.findTaskByFilter(dto, page)), ex -> badRequest().body(ex.getMessage()));
    }

    /**
     * Find all assignees by task id
     *
     * @param taskId the task id
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/find/assignees/by/task")
    public ResponseEntity<?> findAllAssigneesByTaskId(@RequestParam("tid") UUID taskId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_taskService.findAllAssigneesByTaskId(userId, taskId)), ex -> badRequest().body(ex.getMessage()));
    }
}