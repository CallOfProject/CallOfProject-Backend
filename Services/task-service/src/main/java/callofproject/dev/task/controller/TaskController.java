package callofproject.dev.task.controller;

import callofproject.dev.task.dto.request.CreateTaskDTO;
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

    @PostMapping("/complete")
    public ResponseEntity<?> completeTask(@RequestParam("uid") UUID userId, @RequestParam("tid") UUID taskId)
    {
        return subscribe(() -> ok(m_taskService.completeTask(userId, taskId)), ex -> badRequest().body(ex.getMessage()));
    }
}
