package callofproject.dev.service.scheduler.service;

import callofproject.dev.service.scheduler.service.callback.ProjectSchedulerServiceCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@Transactional(transactionManager = "projectDbTransactionManager")
public class ProjectSchedulerService
{
    private final ProjectSchedulerServiceCallback m_serviceCallback;

    public ProjectSchedulerService(ProjectSchedulerServiceCallback serviceCallback)
    {
        m_serviceCallback = serviceCallback;
    }


    @Scheduled(cron = "00 00 02 * * *", zone = "Europe/Istanbul")
    public void checkFeedbackTimeout()
    {
        System.out.println("Checking feedback timeout");
        m_serviceCallback.checkFeedbackTimeout();
    }

    @Scheduled(cron = "00 30 02 * * *", zone = "Europe/Istanbul")
    public void checkProjectDeadlines()
    {
        System.out.println("Checking project deadlines");
        m_serviceCallback.checkProjectDeadlines();
    }

    @Scheduled(cron = "00 00 03 * * *", zone = "Europe/Istanbul")
    public void checkFeedbacks()
    {
        m_serviceCallback.checkFeedbacks();
    }

    @Scheduled(cron = "00 30 03 * * *", zone = "Europe/Istanbul")
    public void checkProjectStartDates()
    {
        System.out.println("Checking project start dates");
        m_serviceCallback.checkProjectStartDates();
    }
}