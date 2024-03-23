package callofproject.dev.service.scheduler.service.callback;

import callofproject.dev.data.common.dto.EmailTopic;
import callofproject.dev.data.common.enums.EmailType;
import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;
import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.data.project.repository.IProjectRepository;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.service.scheduler.config.kafka.KafkaProducer;
import callofproject.dev.service.scheduler.dto.NotificationKafkaDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static callofproject.dev.data.project.entity.enums.EProjectStatus.EXTEND_APPLICATION_FEEDBACK;
import static callofproject.dev.service.scheduler.SchedulerBeanName.PROJECT_SERVICE_PROJECT_REPOSITORY_NAME;
import static callofproject.dev.service.scheduler.util.Util.getEmailTemplate;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toList;

@Service
@Lazy
public class ProjectSchedulerServiceCallback
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final TaskServiceHelper m_taskServiceHelper;
    private final InterviewServiceHelper m_interviewServiceHelper;
    private final UserManagementServiceHelper m_userManagementServiceHelper;
    private final IProjectRepository m_projectRepository;
    private final KafkaProducer m_kafkaProducer;

    @Value("${project.message.started}")
    private String m_projectStartedMessage;

    @Value("${project.message.started.admin-note}")
    private String m_projectStartedAdminNote;

    @Value("${project.message.expired.admin-note}")
    private String m_projectExpiredAdminNote;

    @Value("${project.message.expired}")
    private String m_projectExpiredMessage;

    @Value("${project.message.title.expired}")
    private String m_projectExpiredTitle;

    @Value("${project.message.feedback-extend.title}")
    private String m_feedbackExtendedTitle;

    @Value("${project.message.feedback-extend}")
    private String m_feedbackExtendedMessage;


    public ProjectSchedulerServiceCallback(ProjectServiceHelper projectServiceHelper, TaskServiceHelper taskServiceHelper, InterviewServiceHelper interviewServiceHelper, UserManagementServiceHelper userManagementServiceHelper,
                                           @Qualifier(PROJECT_SERVICE_PROJECT_REPOSITORY_NAME) IProjectRepository projectRepository,
                                           KafkaProducer kafkaProducer)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_taskServiceHelper = taskServiceHelper;
        m_interviewServiceHelper = interviewServiceHelper;
        m_userManagementServiceHelper = userManagementServiceHelper;
        m_projectRepository = projectRepository;
        m_kafkaProducer = kafkaProducer;
    }

    // Check project start dates and Project Status changed to IN_PROGRESS
    public void checkProjectStartDates()
    {
        var projects = toStream(m_projectRepository.findAllByStartDate(now())).toList();
        projects.forEach(this::prepareProjectForStart);
        projects.forEach(p -> sendEmail(p, getEmailTemplate("start_project.html")));
        projects.forEach(p -> sendNotification(p, m_projectStartedAdminNote, m_projectStartedMessage));

        if (!projects.isEmpty())
        {
            var ids = projects.stream().map(Project::getProjectId).toList();
            var projectsInTaskService = ids.stream().map(id -> m_taskServiceHelper.findProjectById(id).get()).toList();
            var projectsInInterviewService = ids.stream().map(id -> m_interviewServiceHelper.findProjectById(id).get()).toList();
            projectsInTaskService.forEach(this::prepareProjectForStartForTaskService);
            projectsInInterviewService.forEach(this::prepareProjectForStartForInterviewService);
        }
    }


    // Check project deadlines and Project Status changed to TIMEOUT
    public void checkProjectDeadlines()
    {
        var projects = toStream(m_projectRepository.findAllByExpectedCompletionDate(now().minusDays(1))).toList();

        projects.forEach(this::expireProject);
        projects.forEach(p -> sendEmailForExpiredProject(p, getEmailTemplate("expired_project.html")));
        projects.forEach(p -> sendNotification(p, m_projectExpiredTitle, m_projectExpiredMessage));

        if (!projects.isEmpty())
        {
            var ids = projects.stream().map(Project::getProjectId).toList();
            var projectsInTaskService = ids.stream().map(id -> m_taskServiceHelper.findProjectById(id).get()).toList();
            var projectsInInterviewService = ids.stream().map(id -> m_interviewServiceHelper.findProjectById(id).get()).toList();
            projectsInTaskService.forEach(this::expireProjectForTaskService);
            projectsInInterviewService.forEach(this::expireProjectForInterviewService);
        }
    }


    // Check feedback timeout and extend feedback date and Project Status changed to EXTEND_APPLICATION_FEEDBACK
    public void checkFeedbackTimeout()
    {
        Predicate<Project> isRequestsNotEmpty = p -> !p.getProjectParticipantRequests().isEmpty();
        var projects = m_projectRepository.findAllByApplicationDeadline(now().minusDays(1)).stream().filter(isRequestsNotEmpty).toList();
        projects.forEach(this::extendFeedbackDate);

        if (!projects.isEmpty())
        {
            var ids = projects.stream().map(Project::getProjectId).toList();
            var projectsInTaskService = ids.stream().map(id -> m_taskServiceHelper.findProjectById(id).get()).toList();
            var projectsInInterviewService = ids.stream().map(id -> m_interviewServiceHelper.findProjectById(id).get()).toList();
            projectsInTaskService.forEach(this::extendFeedbackDateForTaskService);
            projectsInInterviewService.forEach(this::extendFeedbackDateForInterviewService);
        }
    }


    // Check feedback timeout and Project Status changed to APPLICATION_FEEDBACK_TIMEOUT
    public void checkFeedbacks()
    {
        var projects = m_projectRepository.findAllExtendedDateApplications();

        for (var project : projects)
        {
            var requests = project.getProjectParticipantRequests().stream().toList();
            removeParticipantRequestsAndUpdateStatus(requests, project);
            sendNotification(project, "Feedback Timeout", "Feedback timeout for project " + project.getProjectName());
            var template = getEmailTemplate("generic_notification.html");
            var msg = "Your %s named project has been feedback timeout. Your feedback rate decreased!";
            var message = format(template, "Feedback Timeout", project.getProjectOwner().getUsername(), format(msg, project.getProjectName()));
            sendEmail(project.getProjectOwner(), "Feedback Timeout", message);
        }


    }
    // --------------------------------------------------------------------------------------------

    private void extendFeedbackDateForTaskService(callofproject.dev.data.task.entity.Project project)
    {
        project.setProjectStatus(callofproject.dev.data.task.entity.enums.EProjectStatus.EXTEND_APPLICATION_FEEDBACK);
        m_taskServiceHelper.saveProject(project);
    }

    private void extendFeedbackDateForInterviewService(callofproject.dev.data.interview.entity.Project project)
    {
        project.setProjectStatus(callofproject.dev.data.interview.entity.enums.EProjectStatus.EXTEND_APPLICATION_FEEDBACK);
        m_interviewServiceHelper.createProject(project);
    }

    private void prepareProjectForStart(Project project)
    {
        project.setAdminNote(m_projectStartedAdminNote);
        project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        m_projectServiceHelper.saveProject(project);
    }

    private void prepareProjectForStartForTaskService(callofproject.dev.data.task.entity.Project project)
    {
        project.setProjectStatus(callofproject.dev.data.task.entity.enums.EProjectStatus.IN_PROGRESS);
        m_taskServiceHelper.saveProject(project);
    }

    private void prepareProjectForStartForInterviewService(callofproject.dev.data.interview.entity.Project project)
    {
        project.setProjectStatus(callofproject.dev.data.interview.entity.enums.EProjectStatus.IN_PROGRESS);
        m_interviewServiceHelper.createProject(project);
    }

    private void expireProject(Project project)
    {
        project.setProjectStatus(EProjectStatus.TIMEOUT);
        project.setAdminNote(m_projectExpiredAdminNote);
        m_projectServiceHelper.saveProject(project);
    }

    private void expireProjectForTaskService(callofproject.dev.data.task.entity.Project project)
    {
        project.setProjectStatus(callofproject.dev.data.task.entity.enums.EProjectStatus.TIMEOUT);
        m_taskServiceHelper.saveProject(project);
    }

    private void expireProjectForInterviewService(callofproject.dev.data.interview.entity.Project project)
    {
        project.setProjectStatus(callofproject.dev.data.interview.entity.enums.EProjectStatus.TIMEOUT);
        m_interviewServiceHelper.createProject(project);
    }

    private void extendFeedbackDate(Project project)
    {
        var owner = project.getProjectOwner();
        var existingFeedbackDate = project.getApplicationDeadline();
        var existingFeedbackDateStr = toDateString(existingFeedbackDate);
        var extendedFeedbackDate = existingFeedbackDate.plusWeeks(1);
        var extendedFeedbackDateStr = toDateString(extendedFeedbackDate);

        project.setApplicationDeadline(extendedFeedbackDate);
        project.setProjectStatus(EXTEND_APPLICATION_FEEDBACK);
        m_projectServiceHelper.saveProject(project);


        var template = getEmailTemplate("project_extended_feedback_date.html"); // for owner
        var userEmailTemplate = getEmailTemplate("generic_notification.html"); // for participants

        var message = format(template, owner.getUsername(), project.getProjectName(), existingFeedbackDateStr, extendedFeedbackDateStr);
        sendEmail(owner, m_feedbackExtendedTitle, message); // to owner

        for (var participant : project.getProjectParticipants().stream().map(ProjectParticipant::getUser).toList())
        {
            var msg = format(m_feedbackExtendedMessage, project.getProjectName(), existingFeedbackDateStr, extendedFeedbackDateStr);
            // email message
            var emailMessage = format(userEmailTemplate, m_feedbackExtendedTitle, participant.getUsername(), msg);
            sendEmail(participant, m_feedbackExtendedTitle, emailMessage);
            // notify participants
            send(owner.getUserId(), participant.getUserId(), m_feedbackExtendedTitle, msg);
        }
    }


    public void setUserFeedbackRate(String username)
    {
        var userFromAuth = m_userManagementServiceHelper.getUserServiceHelper().findByUsername(username);

        if (userFromAuth.isPresent())
        {
            var user = userFromAuth.get();
            user.getUserProfile().setUserFeedbackRate(user.getUserProfile().getUserFeedbackRate() - .5);
            m_userManagementServiceHelper.getUserServiceHelper().saveUser(user);
        }
    }

    private void removeParticipantRequestsAndUpdateStatus(List<ProjectParticipantRequest> requests, Project project)
    {
        for (var request : requests)
        {
            var user = request.getUser();
            user.getProjectParticipantRequests().removeIf(pr -> pr.getProject().getProjectId().equals(project.getProjectId()));
            m_projectServiceHelper.addUser(user);
        }
        var owner = project.getProjectOwner();
        setUserFeedbackRate(owner.getUsername());
        project.setProjectStatus(EProjectStatus.APPLICATION_FEEDBACK_TIMEOUT);
        m_projectServiceHelper.removeAllParticipantRequests(requests);
        project.getProjectParticipantRequests().clear();
        m_projectServiceHelper.saveProject(project);
    }

    private void sendEmail(User user, String title, String message)
    {
        m_kafkaProducer.sendEmail(new EmailTopic(EmailType.REMAINDER, user.getEmail(), title, message, null));
    }

    private String toDateString(LocalDate date)
    {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
    }


    private void sendEmail(Project project, String template)
    {
        var ownerUsername = project.getProjectOwner().getUsername();
        var startDate = toDateString(project.getStartDate());
        var expectedCompletionDate = toDateString(project.getExpectedCompletionDate());

        var participantList = project.getProjectParticipants().stream().map(ProjectParticipant::getUser).collect(toList());
        participantList.add(project.getProjectOwner());

        for (var user : participantList)
        {
            var msg = format(template, project.getProjectName(), ownerUsername, startDate, expectedCompletionDate);
            sendEmail(user, format(m_projectStartedMessage, project.getProjectName()), msg);
        }
    }

    private void sendEmailForExpiredProject(Project project, String template)
    {
        var ownerUsername = project.getProjectOwner().getUsername();
        var participantList = project.getProjectParticipants().stream().map(ProjectParticipant::getUser).collect(toList());
        participantList.add(project.getProjectOwner());

        for (var user : participantList)
        {
            var msg = format(template, user.getUsername(), project.getProjectName(), ownerUsername);
            sendEmail(user, format(m_projectStartedMessage, project.getProjectName()), msg);
        }
    }

    private void sendNotification(Project project, String title, String message)
    {
        var participants = project.getProjectParticipants();
        var owner = project.getProjectOwner();
        var msg = format(message, project.getProjectName());

        participants.stream().map(ProjectParticipant::getUser).forEach(user -> send(owner.getUserId(), user.getUserId(), title, msg));
    }

    private void send(UUID fromUserId, UUID toUserId, String title, String message)
    {
        var notification = new NotificationKafkaDTO.Builder()
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setNotificationTitle(title)
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationDataType(NotificationDataType.PROJECT)
                .build();

        m_kafkaProducer.sendNotification(notification);
    }
}