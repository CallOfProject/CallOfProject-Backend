package callofproject.dev.task;

import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.repository.ITaskRepository;
import callofproject.dev.task.service.TaskServiceCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public class Injection
{
    @Autowired
    private TaskServiceCallback m_taskServiceCallback;

    @Autowired
    private ITaskRepository m_taskRepository;

    @Autowired
    private TaskServiceHelper m_taskServiceHelper;
    // ----------------------------------------------------------------------------------------------------------------

    public TaskServiceHelper getTaskServiceHelper()
    {
        return m_taskServiceHelper;
    }

    public TaskServiceCallback getTaskServiceCallback()
    {
        return m_taskServiceCallback;
    }

    public ITaskRepository getTaskRepository()
    {
        return m_taskRepository;
    }
}
