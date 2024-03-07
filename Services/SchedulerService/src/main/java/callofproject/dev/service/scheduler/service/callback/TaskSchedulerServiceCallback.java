package callofproject.dev.service.scheduler.service.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static callofproject.dev.data.common.enums.EmailType.REMAINDER;
import static java.lang.String.format;
import static java.time.LocalDate.now;

@Service
@Lazy
public class TaskSchedulerServiceCallback
{
    private static final int REMAINDER_DAYS = 3;
    private static final String REMAINDER_MESSAGE = "You need to complete the task assigned to you, named '%s', within %d days.";
    private final TaskServiceHelper m_taskServiceHelper;
    private final KafkaProducer m_kafkaProducer;

    public TaskSchedulerServiceCallback(TaskServiceHelper taskServiceHelper, KafkaProducer kafkaProducer)
    {
        m_taskServiceHelper = taskServiceHelper;
        m_kafkaProducer = kafkaProducer;
    }

    public void runNotifyUsersForTask()
    {
        var tasks = m_taskServiceHelper.findAllTasksByEnDate(now().minusDays(3));

        for (var task : tasks)
        {
            sendNotifyEmail(task);
            sendNotification(task);
        }
    }


    public void runCloseExpiredTasks()
    {
        var tasks = m_taskServiceHelper.findAllTasksByEnDate(now().minusDays(1));
        tasks.forEach(t -> t.setTaskStatus(TaskStatus.INCOMPLETE));
        m_taskServiceHelper.saveAllTasks(tasks);
    }
    // --------------------------------------------------------------------------------------------

    private void sendNotification(Task task)
    {
        var sender = task.getProject().getProjectOwner();

    }

    private void sendNotifyEmail(Task task)
    {
        var msg = format(REMAINDER_MESSAGE, task.getTitle(), REMAINDER_DAYS);
        var title = "Task Reminder!";
        task.getAssignees().forEach(user -> send(user, msg, title));
    }

    private void send(User user, String msg, String title)
    {
        m_kafkaProducer.sendEmail(new EmailTopic(REMAINDER, user.getEmail(), title, msg, null));
    }
}