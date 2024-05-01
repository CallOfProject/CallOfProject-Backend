package callofproject.dev.service.scheduler.service.match;

import callofproject.dev.service.scheduler.service.match.callback.ProjectRecommendationServiceCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@Transactional(transactionManager = "projectDbTransactionManager")
public class ProjectRecommendationService
{
    private final ProjectRecommendationServiceCallback m_serviceCallback;

    public ProjectRecommendationService(ProjectRecommendationServiceCallback serviceCallback)
    {
        m_serviceCallback = serviceCallback;
    }

    @Scheduled(cron = "0 0 5 ? * SAT", zone = "Europe/Istanbul")
    public void recommendProjectsByUserTagsAndProjectTags()
    {
        m_serviceCallback.recommendProjectByProjectTagAndUserId();
        System.out.println("ProjectRecommendationService.recommendProjectsByUserTags");
    }
}
