package callofproject.dev.task.controller;

import callofproject.dev.task.dto.request.ChangeTaskPriorityDTO;
import callofproject.dev.task.dto.request.ChangeTaskStatusDTO;
import callofproject.dev.task.dto.request.CreateTaskDTO;
import callofproject.dev.task.dto.request.UpdateTaskDTO;
import callofproject.dev.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/task")
public class TaskController
{
    private final TaskService m_taskService;

    public TaskController(TaskService taskService)
    {
        m_taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO)
    {
        return subscribe(() -> ok(m_taskService.createTask(createTaskDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody @Valid UpdateTaskDTO updateTaskDTO)
    {
        return subscribe(() -> ok(m_taskService.updateTask(updateTaskDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestParam("tid") UUID taskId)
    {
        return subscribe(() -> ok(m_taskService.deleteTask(taskId)), ex -> badRequest().body(ex.getMessage()));
    }

    @DeleteMapping("/delete/by/task-and-user")
    public ResponseEntity<?> deleteTask(@RequestParam("tid") UUID taskId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_taskService.deleteTaskByTaskIdAndUserId(userId, taskId)), ex -> badRequest().body(ex.getMessage()));
    }

    @PutMapping("/change/status")
    public ResponseEntity<?> changeTaskStatus(@RequestBody ChangeTaskStatusDTO taskStatusDTO)
    {
        return subscribe(() -> ok(m_taskService.changeTaskStatus(taskStatusDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    @PutMapping("/change/priority")
    public ResponseEntity<?> changeTaskPriority(@RequestBody ChangeTaskPriorityDTO priorityDTO)
    {
        return subscribe(() -> ok(m_taskService.changeTaskPriority(priorityDTO)), ex -> badRequest().body(ex.getMessage()));
    }

    @GetMapping("/find/by/project")
    public ResponseEntity<?> findTaskByProjectId(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_taskService.findTasksByProjectId(projectId)), ex -> badRequest().body(ex.getMessage()));
    }

    @GetMapping("/find/by/project-and-user")
    public ResponseEntity<?> findTasksByProjectIdAndUserId(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_taskService.findTasksByProjectIdAndUserId(projectId, userId)), ex -> badRequest().body(ex.getMessage()));
    }

    @GetMapping("/find/by/id")
    public ResponseEntity<?> findTaskById(@RequestParam("tid") UUID taskId)
    {
        return subscribe(() -> ok(m_taskService.findTaskById(taskId)), ex -> badRequest().body(ex.getMessage()));
    }

    // projedeki tüm taskleri getir
    // projedeki taskleri kullanıcıya göre getir
}
