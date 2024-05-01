package callofproject.dev.service.scheduler.service.match;

import callofproject.dev.service.scheduler.service.match.callback.UserMatchingServiceCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@Transactional(transactionManager = "authenticationDbTransactionManager")
public class UserMatchingService
{

    private final UserMatchingServiceCallback m_userMatchingServiceCallback;

    public UserMatchingService(UserMatchingServiceCallback userMatchingServiceCallback)
    {
        m_userMatchingServiceCallback = userMatchingServiceCallback;
    }

    @Scheduled(cron = "0 0 6 ? * WED", zone = "Europe/Istanbul")
    public void matchUsersByTag()
    {
        m_userMatchingServiceCallback.matchUsersByUserTags();
        System.out.println("UserMatchingService.checkStartedTestInterviews");
    }

    @Scheduled(cron = "0 0 6 ? * FRI", zone = "Europe/Istanbul")
    public void matchUsersByEducation()
    {
        m_userMatchingServiceCallback.matchUsersByEducation();
        System.out.println("UserMatchingService.checkStartedTestInterviews");
    }

    @Scheduled(cron = "0 0 6 ? * SUN", zone = "Europe/Istanbul")
    public void matchUsersByExperience()
    {
        m_userMatchingServiceCallback.matchUsersByExperience();
        System.out.println("UserMatchingService.checkStartedTestInterviews");
    }
}