package callofproject.dev.service.scheduler.service;

import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.data.project.repository.IProjectRepository;
import callofproject.dev.library.exception.service.DataServiceException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static callofproject.dev.data.project.entity.enums.EProjectStatus.EXTEND_APPLICATION_FEEDBACK;
import static java.lang.String.format;
import static java.time.LocalDate.now;

@Service
@EnableScheduling
@Transactional(transactionManager = "projectDbTransactionManager")
public class FeedbackSchedulerService
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final IProjectRepository m_projectRepository;

    public FeedbackSchedulerService(ProjectServiceHelper projectServiceHelper, IProjectRepository projectRepository)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_projectRepository = projectRepository;
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
        }

        else if (project.getProjectStatus() != EXTEND_APPLICATION_FEEDBACK && project.getProjectStatus() != EProjectStatus.APPLICATION_FEEDBACK_TIMEOUT)
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

    @Scheduled(cron = "0 00 03 * * *", zone = "Europe/Istanbul")
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
}
