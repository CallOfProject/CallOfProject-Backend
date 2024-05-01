package callofproject.dev.service.scheduler.service.match.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import callofproject.dev.service.scheduler.dto.UserMatchDTO;
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
public class UserMatchingServiceCallback
{
    private final IMatchingAndRecommendationService m_userMatchingService;
    private final UserManagementServiceHelper m_serviceHelper;
    private final KafkaProducer m_kafkaProducer;

    @Value("${service.user-match-scheduler-service.user-link}")
    private String m_userProfileLink;

    public UserMatchingServiceCallback(IMatchingAndRecommendationService userMatchingService, UserManagementServiceHelper serviceHelper, KafkaProducer kafkaProducer)
    {
        m_userMatchingService = userMatchingService;
        m_serviceHelper = serviceHelper;
        m_kafkaProducer = kafkaProducer;
    }

    private void recommendUsersByUserTags(User user)
    {
        var recommendedUserIds = m_userMatchingService.recommendUsersByUserTags(user.getUserId().toString())
                .stream()
                .map(UUID::fromString)
                .toList();

        var result = toStream(m_serviceHelper.getUserServiceHelper().findAllByUserIds(recommendedUserIds))
                .map(this::toUserMatchDTO)
                .toList();

        if (!result.isEmpty())
            sendEmail(result, user, "Follow Recommended Users for your tags");
    }

    private void recommendUsersByExperience(User user)
    {
        var recommendedUserIds = m_userMatchingService.recommendUsersByExperience(user.getUserId().toString())
                .stream()
                .map(UUID::fromString)
                .toList();

        var result = toStream(m_serviceHelper.getUserServiceHelper().findAllByUserIds(recommendedUserIds))
                .map(this::toUserMatchDTO)
                .toList();

        if (!result.isEmpty())
            sendEmail(result, user, "Follow Recommended Users according to your experience");
    }

    private void recommendUsersByEducation(User user)
    {
        var recommendedUserIds = m_userMatchingService.recommendUsersByEducation(user.getUserId().toString())
                .stream()
                .peek(System.out::println)
                .map(UUID::fromString)
                .toList();

        var result = toStream(m_serviceHelper.getUserServiceHelper().findAllByUserIds(recommendedUserIds))
                .map(this::toUserMatchDTO)
                .toList();

        if (!result.isEmpty())
            sendEmail(result, user, "Follow Recommended Users according to your education");
    }

    private UserMatchDTO toUserMatchDTO(User user)
    {
        var link = m_userProfileLink.formatted(user.getUserId().toString());
        return new UserMatchDTO(user.getUsername(), user.getUserId(), user.getFirstName(), user.getMiddleName(),
                user.getLastName(), link, user.getEmail(), user.getUserProfile().getProfilePhoto());
    }

    private void sendEmail(List<UserMatchDTO> users, User owner, String title)
    {
        var emailTemplate = getEmailTemplate("match_user.html");
        var emailContent = emailTemplate.formatted(title, createDetailsTemplate(users));
        m_kafkaProducer.sendEmail(new EmailTopic(EmailType.REMAINDER, owner.getEmail(), title, emailContent, null));
    }


    private String createDetailsTemplate(List<UserMatchDTO> users)
    {
        var sb = new StringBuilder();

        for (var user : users)
        {
            sb.append("<div class=\"details\"><img style=\" width: 50px; height: 50px;\" src=\"%s\" alt=\"user_image\"><p><strong>%s</strong></p><button><a style=\"color:white;\" href=\"%s\">Follow</a></button></div>".formatted(user.image(), user.username(), user.link())).append("\n");
        }
        return sb.toString();
    }
    // ------------------- Public Methods -------------------

    public void matchUsersByUserTags()
    {
        var allUsers = toStream(m_serviceHelper.getUserServiceHelper().findAll());

        allUsers.forEach(this::recommendUsersByUserTags);
    }

    public void matchUsersByEducation()
    {
        var allUsers = toStream(m_serviceHelper.getUserServiceHelper().findAll());

        allUsers.forEach(this::recommendUsersByEducation);
    }


    public void matchUsersByExperience()
    {
        var allUsers = toStream(m_serviceHelper.getUserServiceHelper().findAll());

        allUsers.forEach(this::recommendUsersByExperience);
    }
}
