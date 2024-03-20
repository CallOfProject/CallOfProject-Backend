package callofproject.dev.task;

import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.repository.ITaskRepository;
import callofproject.dev.data.task.repository.IUserRepository;
import callofproject.dev.task.mapper.IUserMapper;
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
    private IUserRepository m_userRepository;

    @Autowired
    private TaskServiceHelper m_taskServiceHelper;

    @Autowired
    private IUserMapper m_userMapper;

    public IUserMapper getUserMapper()
    {
        return m_userMapper;
    }
    public TaskServiceHelper getTaskServiceHelper()
    {
        return m_taskServiceHelper;
    }

    public IUserRepository getUserRepository()
    {
        return m_userRepository;
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
