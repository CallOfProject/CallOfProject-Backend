package callofproject.dev.service.scheduler.service.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.data.project.repository.IProjectRepository;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

import static callofproject.dev.data.project.entity.enums.EProjectStatus.EXTEND_APPLICATION_FEEDBACK;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.lang.String.format;
import static java.time.LocalDate.now;

@Service
@Lazy
public class ProjectSchedulerServiceCallback
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final IProjectRepository m_projectRepository;
    private final KafkaProducer m_kafkaProducer;
    private final ExecutorService m_executorService;

    private final String START_PROJECT_EMAIL_TITLE = "%s named project is started!";


    public ProjectSchedulerServiceCallback(ProjectServiceHelper projectServiceHelper, @Qualifier("callofproject.dev.data.project.repository.IProjectRepository") IProjectRepository projectRepository, KafkaProducer kafkaProducer, ExecutorService executorService)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_projectRepository = projectRepository;
        m_kafkaProducer = kafkaProducer;
        m_executorService = executorService;
    }


    public String getEmailTemplate(String templateName) throws IOException
    {
        // HTML dosyasını oku ve bir dizeye dönüştür
        ClassPathResource resource = new ClassPathResource(templateName);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        String templateContent = FileCopyUtils.copyToString(reader);
        reader.close();

        return templateContent;
    }

    public void removeParticipantCallback(UUID projectId, UUID userId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var user = findUserIfExists(userId);
        var participant = m_projectServiceHelper.findProjectParticipantByUserIdAndProjectId(userId, projectId);

        if (participant.isEmpty())
            throw new DataServiceException("Participant is not found!");

        project.getProjectParticipants().remove(participant.get());
        user.getProjectParticipants().remove(participant.get());
        m_projectServiceHelper.saveProject(project);
        m_projectServiceHelper.addUser(user);
        m_projectServiceHelper.deleteProjectParticipant(participant.get());
    }

    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    private User findUserIfExists(UUID userId)
    {
        var user = m_projectServiceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", userId));

        return user.get();
    }

    private void removeParticipantRequests(List<ProjectParticipantRequest> requests, Project project)
    {
        if (project.getProjectStatus() == EXTEND_APPLICATION_FEEDBACK)
        {
            project.setProjectStatus(EProjectStatus.APPLICATION_FEEDBACK_TIMEOUT);
            project.setAdminNote("Project Application Feedback Timeout!");
        } else if (project.getProjectStatus() != EXTEND_APPLICATION_FEEDBACK && project.getProjectStatus() != EProjectStatus.APPLICATION_FEEDBACK_TIMEOUT)
        {
            project.setProjectStatus(EXTEND_APPLICATION_FEEDBACK);
            project.setApplicationDeadline(project.getApplicationDeadline().plusWeeks(1));
            project.setAdminNote("Project Application Feedback Date Extended One Week!");
        }

        m_projectServiceHelper.saveProject(project);

        if (project.getProjectStatus() == EProjectStatus.APPLICATION_FEEDBACK_TIMEOUT)
        {
            for (var request : requests)
            {
                var user = findUserIfExists(request.getUser().getUserId());
                project.getProjectParticipantRequests().remove(request);
                user.getProjectParticipantRequests().remove(request);
                m_projectServiceHelper.saveProject(project);
                m_projectServiceHelper.addUser(user);
                m_projectServiceHelper.removeParticipantRequestByRequestId(request.getParticipantRequestId());
            }
        }
    }

    public void checkFeedbacks()
    {
        Predicate<Project> isRequestNotEmpty = p -> !p.getProjectParticipantRequests().isEmpty();

        var projects = m_projectRepository.findAllByApplicationDeadlineBefore(now()).stream().filter(isRequestNotEmpty).toList();

        for (var project : projects)
        {
            var requests = project.getProjectParticipantRequests().stream().toList();
            removeParticipantRequests(requests, project);
        }
    }

    public void checkProjectDeadlines()
    {
        var projects = toStream(m_projectRepository.findAllByExpectedCompletionDate(now().minusDays(1))).toList();
        System.out.println(projects.size());

        var title = "Project Time Expired";
        var message = "Project Time Expired";
        for (var project : projects)
        {
            project.setProjectStatus(EProjectStatus.TIMEOUT);
            project.setAdminNote("Project time is expired!");
            m_projectServiceHelper.saveProject(project);
            sendEmailToProjectParticipants(project, title, message);
        }
    }

    private void sendEmailToProjectParticipants(Project project, String title, String message)
    {
        var participants = project.getProjectParticipants().stream().toList();


        for (var participant : participants)
        {
            var user = participant.getUser();
            sendEmail(user, title, message);
        }
    }

    private void sendEmail(User user, String title, String message)
    {
        m_kafkaProducer.sendEmail(new EmailTopic(EmailType.REMAINDER, user.getEmail(), title, message, null));
    }

    private String toDateString(LocalDate date)
    {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
    }

    public void checkProjectStartDates()
    {
        var projects = toStream(m_projectRepository.findAllByStartDate(now())).toList();
        for (Project project : projects)
        {
            try
            {
                prepareProjectForStart(project);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void prepareProjectForStart(Project project) throws IOException
    {
        project.setAdminNote("Project is started!");
        project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        m_projectServiceHelper.saveProject(project);

        var ownerUsername = project.getProjectOwner().getUsername();
        var startDate = toDateString(project.getStartDate());
        var expectedCompletionDate = toDateString(project.getExpectedCompletionDate());
        var msg = format(getEmailTemplate("start_project.html"), project.getProjectName(), ownerUsername, startDate, expectedCompletionDate);
        sendEmailToProjectParticipants(project, format(START_PROJECT_EMAIL_TITLE, project.getProjectName()), msg);
    }
}
