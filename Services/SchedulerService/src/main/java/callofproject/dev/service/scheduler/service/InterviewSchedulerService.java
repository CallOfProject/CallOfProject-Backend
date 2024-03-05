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

    @Scheduled(cron = "*/10 * * * * *", zone = "Europe/Istanbul")
    public void checkExpiredTestInterviews()
    {
        System.out.println("Checking expired test interviews");
        m_serviceCallback.checkExpiredTestInterviews();
    }

    @Scheduled(cron = "*/30 * * * * *", zone = "Europe/Istanbul")
    public void checkExpiredCodingInterviews()
    {
        System.out.println("Checking expired coding interviews");
        m_serviceCallback.checkExpiredCodingInterviews();
    }
}