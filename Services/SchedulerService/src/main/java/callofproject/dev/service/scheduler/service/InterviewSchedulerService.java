package callofproject.dev.service.scheduler.service;

import callofproject.dev.service.scheduler.service.callback.InterviewSchedulerServiceCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@Transactional(transactionManager = "interviewDbTransactionManager")
public class InterviewSchedulerService
{
    private final InterviewSchedulerServiceCallback m_serviceCallback;

    public InterviewSchedulerService(InterviewSchedulerServiceCallback serviceCallback)
    {
        m_serviceCallback = serviceCallback;
    }

    @Scheduled(cron = "00 00 00 * * *", zone = "Europe/Istanbul")
    public void checkExpiredTestInterviews()
    {
        m_serviceCallback.checkExpiredTestInterviews();
    }

    @Scheduled(cron = "00 30 00 * * *", zone = "Europe/Istanbul")
    public void checkExpiredCodingInterviews()
    {
        m_serviceCallback.checkExpiredCodingInterviews();
    }

    @Scheduled(cron = "00 00 01 * * *", zone = "Europe/Istanbul")
    public void reminderTestInterviews()
    {
        m_serviceCallback.reminderTestInterviews();
    }

    @Scheduled(cron = "00 30 01 * * *", zone = "Europe/Istanbul")
    public void reminderCodingInterviews()
    {
        m_serviceCallback.reminderCodingInterviews();
    }
}