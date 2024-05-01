package callofproject.dev.service.scheduler.service.match.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import callofproject.dev.service.scheduler.dto.RecommendedProjectDTO;
import callofproject.dev.service.scheduler.service.match.remote.IMatchingAndRecommendationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.service.scheduler.util.Util.getEmailTemplate;
import static callofproject.dev.util.stream.StreamUtil.toStream;

@Service
@Lazy
public class ProjectRecommendationServiceCallback
{
    private final IMatchingAndRecommendationService m_userMatchingService;
    private final ProjectServiceHelper m_serviceHelper;
    private final UserManagementServiceHelper m_userManagementServiceHelper;
    private final KafkaProducer m_kafkaProducer;

    @Value("${service.recommend-project-scheduler-service.project-link}")
    private String m_projectLink;

    @Value("${recommended-project-count}")
    private int m_projectCount;

    public ProjectRecommendationServiceCallback(IMatchingAndRecommendationService userMatchingService, ProjectServiceHelper serviceHelper, UserManagementServiceHelper userManagementServiceHelper, KafkaProducer kafkaProducer)
    {
        m_userMatchingService = userMatchingService;
        m_serviceHelper = serviceHelper;
        m_userManagementServiceHelper = userManagementServiceHelper;
        m_kafkaProducer = kafkaProducer;
    }

    private void recommendProjectsByUserTagsAndProjectTags(User user)
    {
        var recommendedUserIds = m_userMatchingService.recommendProjectsByProjectTagAndUserTags(user.getUserId().toString())
                .stream()
                .map(UUID::fromString)
                .toList();

        var result = toStream(m_serviceHelper.findAllProjectByIds(recommendedUserIds))
                .filter(p -> p.getProjectParticipants().stream().map(ProjectParticipant::getUser).noneMatch(pp -> pp.getUserId().equals(user.getUserId())))
                .map(this::toRecommendedProjectDTO)
                .limit(m_projectCount)
                .toList();

        if (!result.isEmpty())
            sendEmail(result, user, "Recommended Projects for your abilities and interests");
    }

    private RecommendedProjectDTO toRecommendedProjectDTO(Project project)
    {
        var link = m_projectLink.formatted(project.getProjectId().toString());
        return new RecommendedProjectDTO(project.getProjectId(), project.getProjectName(), project.getProjectImagePath(),
                link, project.getProjectSummary().substring(0, Math.min(project.getProjectSummary().length(), 60)) + "...");
    }


    private void sendEmail(List<RecommendedProjectDTO> projects, User owner, String title)
    {
        var emailTemplate = getEmailTemplate("recommendation_projects.html");
        var emailContent = emailTemplate.formatted(title, createDetailsTemplate(projects));
        m_kafkaProducer.sendEmail(new EmailTopic(EmailType.REMAINDER, owner.getEmail(), title, emailContent, null));
    }


    private String createDetailsTemplate(List<RecommendedProjectDTO> projects)
    {
        var sb = new StringBuilder();

        for (var project : projects)
        {
            sb.append("<div class=\"details\"><img style=\" width: 50px; height: 50px;\" src=\"%s\" alt=\"project_image\"><p><strong>%s</strong></p><p>%s</p><button><a style=\"color:white;\" href=\"%s\">Go to Project</a></button></div>".formatted(project.projectImage(), project.projectName(), project.summary(), project.link())).append("\n");
        }
        return sb.toString();
    }
    // ------------------- Public Methods -------------------

    public void recommendProjectByProjectTagAndUserId()
    {
        var allProjects = toStream(m_userManagementServiceHelper.getUserServiceHelper().findAll());

        allProjects.forEach(this::recommendProjectsByUserTagsAndProjectTags);
    }
}
