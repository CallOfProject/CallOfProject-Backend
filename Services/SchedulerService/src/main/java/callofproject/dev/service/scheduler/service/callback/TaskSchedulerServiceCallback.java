package callofproject.dev.service.scheduler.service.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import callofproject.dev.service.scheduler.dto.NotificationKafkaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.data.common.enums.EmailType.REMAINDER;
import static callofproject.dev.service.scheduler.util.Util.getEmailTemplate;
import static java.lang.String.format;
import static java.time.LocalDate.now;

@Service
@Lazy
public class TaskSchedulerServiceCallback
{
    private final TaskServiceHelper m_taskServiceHelper;
    private final KafkaProducer m_kafkaProducer;

    @Value("${task.email.template.path}")
    private String m_emailTemplate;

    @Value("${task.email.expired.title}")
    private String m_expiredTitle;

    @Value("${task.email.reminder.title}")
    private String m_reminderTitle;

    @Value("${task.reminder.message-content}")
    private String m_reminderMessageContent;

    @Value("${task.expired.message-content}")
    private String m_expiredMessageContent;

    @Value("${task.reminder-date-from-now}")
    private int m_taskReminderDateFromNow;


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
            sendEmail(task, m_reminderTitle, getEmailTemplate(m_emailTemplate).trim());
            sendNotification(task, m_reminderTitle, m_reminderMessageContent);
        }
    }


    public void runCloseExpiredTasks()
    {
        var tasks = m_taskServiceHelper.findAllTasksByEnDate(now().minusDays(1));
        tasks.forEach(t -> t.setTaskStatus(TaskStatus.INCOMPLETE));
        m_taskServiceHelper.saveAllTasks(tasks);

        for (Task t : tasks)
        {
            sendEmail(t, m_expiredTitle, getEmailTemplate(m_emailTemplate).trim());
            sendNotification(t, m_expiredTitle, m_expiredMessageContent);
        }
    }
    // --------------------------------------------------------------------------------------------

    private void sendEmail(Task task, String title, String emailTemplate)
    {
        var project = task.getProject();
        var owner = project.getProjectOwner();

        for (var user : task.getAssignees())
        {
            var message = format(emailTemplate, user.getUsername(), task.getTitle(), project.getProjectName(), owner.getUsername());
            m_kafkaProducer.sendEmail(new EmailTopic(REMAINDER, user.getEmail(), title, message, null));
        }
    }

    private void sendNotification(Task task, String title, String msg)
    {
        var project = task.getProject();
        var owner = project.getProjectOwner();

        for (var user : task.getAssignees())
        {
            var message = format(msg, user.getUsername(), task.getTitle(), project.getProjectName(), owner.getUsername());

            sendNotification(owner.getUserId(), user.getUserId(), title, message);
        }
    }

    private void sendNotification(UUID fromUserId, UUID toUserId, String title, String message)
    {
        var notification = new NotificationKafkaDTO.Builder()
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setNotificationTitle(title)
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationDataType(NotificationDataType.INTERVIEW)
                .build();

        m_kafkaProducer.sendNotification(notification);
    }
}