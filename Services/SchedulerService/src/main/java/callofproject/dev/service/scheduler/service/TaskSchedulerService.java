package callofproject.dev.service.scheduler.service;

import callofproject.dev.service.scheduler.service.callback.TaskSchedulerServiceCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@Transactional(transactionManager = "taskDbTransactionManager")
public class TaskSchedulerService
{
    private final TaskSchedulerServiceCallback m_schedulerServiceCallback;

    public TaskSchedulerService(TaskSchedulerServiceCallback schedulerServiceCallback)
    {
        m_schedulerServiceCallback = schedulerServiceCallback;
    }


    @Scheduled(cron = "00 00 04 * * *", zone = "Europe/Istanbul")
    public void notifyUsersForTask()
    {
        m_schedulerServiceCallback.runNotifyUsersForTask();
    }


    @Scheduled(cron = "00 30 04 * * *", zone = "Europe/Istanbul")
    public void closeExpiredTasks()
    {
        m_schedulerServiceCallback.runCloseExpiredTasks();
    }
}