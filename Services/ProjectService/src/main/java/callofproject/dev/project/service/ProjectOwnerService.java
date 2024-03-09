package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.NotificationServiceHelper;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.enums.NotificationType;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.common.status.Status.*;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toList;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.lang.String.format;

/**
 * Service class for project owner-related operations.
 * It implements the IProjectOwnerService interface.
 */
@Service
@Lazy
public class ProjectOwnerService implements IProjectOwnerService
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final ObjectMapper m_objectMapper;
    private final KafkaProducer m_kafkaProducer;
    private final IProjectMapper m_projectMapper;
    private final IProjectParticipantMapper m_projectParticipantMapper;
    private final NotificationServiceHelper m_notificationServiceHelper;
    @Value("${notification.request.approve}")
    private String m_approvalLink;

    /**
     * Constructor for ProjectOwnerService.
     *
     * @param projectServiceHelper      The ProjectServiceHelper to be injected.
     * @param projectTagServiceHelper   The ProjectTagServiceHelper to be injected.
     * @param objectMapper              The ObjectMapper to be injected.
     * @param kafkaProducer             The KafkaProducer to be injected.
     * @param projectMapper             The IProjectMapper to be injected.
     * @param projectParticipantMapper  The IProjectParticipantMapper to be injected.
     * @param notificationServiceHelper The NotificationServiceHelper to be injected.
     */
    public ProjectOwnerService(ProjectServiceHelper projectServiceHelper, ProjectTagServiceHelper projectTagServiceHelper, ObjectMapper objectMapper, KafkaProducer kafkaProducer, IProjectMapper projectMapper, IProjectParticipantMapper projectParticipantMapper, NotificationServiceHelper notificationServiceHelper)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_objectMapper = objectMapper;
        m_kafkaProducer = kafkaProducer;
        m_projectMapper = projectMapper;
        m_projectParticipantMapper = projectParticipantMapper;
        m_notificationServiceHelper = notificationServiceHelper;
    }


    /**
     * Adds a participant to a project.
     *
     * @param dto Data Transfer Object containing details for saving a project participant.
     * @return A ResponseMessage containing a Boolean result of the operation.
     */
    @Override
    public ResponseMessage<Boolean> addParticipant(SaveProjectParticipantDTO dto)
    {
        var project = findProjectIfExistsByProjectId(dto.project_id());
        var user = findUserIfExists(dto.user_id());
        var response = doForDataService(() -> m_projectServiceHelper.addProjectParticipant(new ProjectParticipant(project, user)),
                "ProjectService::addParticipant");

        if (response != null)
        {
            m_kafkaProducer.sendProjectParticipant(new ProjectParticipantKafkaDTO(response.getProjectId(), response.getProject().getProjectId(), response.getUser().getUserId(), response.getJoinDate(), false));
            return new ResponseMessage<>("Participant added to project!", OK, true);
        }

        return new ResponseMessage<>("Participant is already added to project!", NOT_ACCEPTED, false);
    }

    /**
     * Removes a participant from a project.
     *
     * @param projectId The UUID of the project.
     * @param userId    The UUID of the user to be removed.
     * @return A ResponseMessage containing the result of the operation.
     */
    @Override
    public ResponseMessage<Object> removeParticipant(UUID projectId, UUID userId)
    {
        return doForDataService(() -> removeParticipantCallback(projectId, userId), "ProjectService::removeParticipant");
    }


    /**
     * Approves a participant's request to join a project.
     *
     * @param requestDTO Data Transfer Object containing the participant request details.
     * @return A ResponseMessage containing the result of the approval operation.
     */
    @Override
    public ResponseMessage<Object> approveParticipantRequest(ParticipantRequestDTO requestDTO)
    {
        var result = doForDataService(() -> approveParticipantRequestCallback(requestDTO), "ProjectService::approveParticipantRequest");

        if (result.getStatusCode() == ACCEPTED && result.getObject() instanceof ParticipantStatusDTO dto && dto.isAccepted())
        {
            var message = format("%s accepted your request to join %s project!", dto.owner().getFullName(), dto.project().getProjectName());
            sendNotificationToUser(dto.project(), dto.user(), dto.owner(), message);
            var info = result.getObject() instanceof ParticipantStatusDTO statusDTO ? statusDTO : null;
            sendParticipantsInfo(info);
        }
        if (result.getStatusCode() == NOT_ACCEPTED && result.getObject() instanceof ParticipantStatusDTO dto && !dto.isAccepted())
        {
            var message = format("%s denied your request to join %s project!", dto.owner().getFullName(), dto.project().getProjectName());
            sendNotificationToUser(dto.project(), dto.user(), dto.owner(), message);
        }
        m_notificationServiceHelper.deleteNotificationById(requestDTO.notificationId());
        return new ResponseMessage<>(result.getMessage(), result.getStatusCode(), result.getStatusCode() == ACCEPTED);
    }

    private void sendParticipantsInfo(ParticipantStatusDTO info)
    {
        var participant = m_projectServiceHelper.findProjectParticipantByUserIdAndProjectId(info.user().getUserId(), info.project().getProjectId());
        m_kafkaProducer.sendProjectParticipant(new ProjectParticipantKafkaDTO(participant.get().getProjectId(), participant.get().getProject().getProjectId(),
                participant.get().getUser().getUserId(), participant.get().getJoinDate(), false));
    }

    /**
     * Marks a project as finished.
     *
     * @param userId    The UUID of the user who is finishing the project.
     * @param projectId The UUID of the project to be finished.
     * @return A ResponseMessage containing the result of the finish operation.
     */
    @Override
    public ResponseMessage<Object> finishProject(UUID userId, UUID projectId)
    {
        return doForDataService(() -> finishProjectCallback(userId, projectId), "ProjectService::finishProject");
    }

    /**
     * Changes the status of a project.
     *
     * @param userId        The UUID of the user changing the project status.
     * @param projectId     The UUID of the project whose status is being changed.
     * @param projectStatus The new status of the project.
     * @return A ResponseMessage containing the result of the status change.
     */
    @Override
    public ResponseMessage<Object> changeProjectStatus(UUID userId, UUID projectId, EProjectStatus projectStatus)
    {
        return doForDataService(() -> changeProjectStatusCallback(userId, projectId, projectStatus), "ProjectService::changeProjectStatus");
    }

    /**
     * Removes a project.
     *
     * @param userId    The UUID of the user removing the project.
     * @param projectId The UUID of the project to be removed.
     * @return A ResponseMessage containing the result of the remove operation.
     */
    @Override
    public ResponseMessage<Object> removeProject(UUID userId, UUID projectId)
    {
        return doForDataService(() -> removeProjectCallback(userId, projectId), "ProjectService::removeProject");
    }

    /**
     * Callback for approving a participant's request to join a project.
     *
     * @param requestDTO Data Transfer Object containing the participant request details.
     * @return A ResponseMessage containing the result of the approval operation callback.
     */
    @Override
    public ResponseMessage<Object> approveParticipantRequestCallback(ParticipantRequestDTO requestDTO)
    {
        var participantRequest = findProjectParticipantRequestByRequestId(requestDTO.requestId());
        var project = participantRequest.getProject();
        var user = participantRequest.getUser();
        var projectOwner = project.getProjectOwner();

        project.getProjectParticipantRequests().remove(participantRequest);
        user.getProjectParticipantRequests().remove(participantRequest);

        m_projectServiceHelper.saveProject(project);
        m_projectServiceHelper.addUser(user);
        m_projectServiceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId());

        var isExistsUser = project.getProjectParticipants()
                .stream()
                .anyMatch(p -> p.getUser().getUserId().equals(user.getUserId()) && p.getProject().getProjectId().equals(project.getProjectId()));


        if (isExistsUser)
            return new ResponseMessage<>("User is already participant in this project!", NOT_ACCEPTED, false);


        if (!requestDTO.isAccepted()) // if not  accepted then call denied
            return deniedParticipantRequest(user, project, projectOwner);

        return approveParticipant(user, project, projectOwner);
    }

    /**
     * Callback for removing a participant from a project.
     *
     * @param projectId The UUID of the project.
     * @param userId    The UUID of the user to be removed.
     * @return A ResponseMessage containing the result of the remove operation callback.
     */
    @Override
    public ResponseMessage<Object> removeParticipantCallback(UUID projectId, UUID userId)
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

        m_kafkaProducer.sendProjectParticipant(new ProjectParticipantKafkaDTO(participant.get().getProjectId(), projectId, userId, participant.get().getJoinDate(), true));
        var message = format("%s removed you from %s project!", project.getProjectOwner().getFullName(), project.getProjectName());

        return new ResponseMessage<>(message, OK, true);
    }

    /**
     * Creates a response message indicating the denial of a participant request for a project.
     *
     * @param user         The user who requested to join the project.
     * @param project      The project for which the join request was made.
     * @param projectOwner The owner of the project.
     * @return A ResponseMessage object containing the denial status and additional details.
     */
    private ResponseMessage<Object> deniedParticipantRequest(User user, Project project, User projectOwner)
    {
        return new ResponseMessage<>("Participant request is not accepted!", NOT_ACCEPTED, new ParticipantStatusDTO(project, user, projectOwner, false));
    }

    /**
     * Approves a participant request for a project and updates the project and user details accordingly.
     *
     * @param user    The user who requested to join the project.
     * @param project The project for which the join request was made.
     * @param owner   The owner of the project.
     * @return A ResponseMessage object indicating the successful approval of the participant.
     */
    private ResponseMessage<Object> approveParticipant(User user, Project project, User owner)
    {
        // Add participant to project
        project.addProjectParticipant(new ProjectParticipant(project, user));
        m_projectServiceHelper.saveProject(project);

        // update user
        user.setParticipantProjectCount(user.getParticipantProjectCount() + 1);
        user.setTotalProjectCount(user.getTotalProjectCount() + 1);
        m_projectServiceHelper.addUser(user);
        return new ResponseMessage<>("Participant request is accepted!", ACCEPTED, new ParticipantStatusDTO(project, user, owner, true));
    }

    /**
     * Sends a notification to a user regarding a project-related action.
     *
     * @param project The project associated with the notification.
     * @param user    The recipient of the notification.
     * @param owner   The owner of the project, typically the sender of the notification.
     * @param message The message to be included in the notification.
     */
    private void sendNotificationToUser(Project project, User user, User owner, String message)
    {
        var data = new NotificationObject(project.getProjectId(), user.getUserId());
        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

       /* var notificationMessage = new ProjectParticipantRequestDTO.Builder()
                .setFromUserId(user.getUserId())
                .setToUserId(project.getProjectOwner().getUserId())
                .setMessage(message)
                .setNotificationType(NotificationType.REQUEST)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .setNotificationImage(null)
                .setNotificationTitle("Project Join Request")
                .setNotificationDataType(NotificationDataType.PROJECT_JOIN_REQUEST)
                .setApproveLink(m_approvalLink)
                .setRejectLink(null)
                .setRequestId(request.getParticipantRequestId())
                .build();*/

        var notificationMessage = new ProjectParticipantNotificationDTO.Builder()
                .setFromUserId(owner.getUserId())
                .setToUserId(user.getUserId())
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .build();
        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage), "ProjectService::approveParticipantRequest");
    }

    /**
     * Retrieves a project participant request by its unique request ID.
     *
     * @param requestId The UUID of the participant request.
     * @return The ProjectParticipantRequest object if found.
     * @throws DataServiceException if the request is not found or already accepted.
     */
    private ProjectParticipantRequest findProjectParticipantRequestByRequestId(UUID requestId)
    {
        var request = doForDataService(() -> m_projectServiceHelper.findProjectParticipantRequestByParticipantRequestId(requestId),
                "ProjectService::findProjectParticipantRequestByRequestId");

        if (request.isEmpty())
            throw new DataServiceException("Participant request is not found!");

        if (request.get().isAccepted())
            throw new DataServiceException("Participant request is already accepted!");

        return request.get();
    }

    /**
     * Finds and retrieves a list of tags associated with a given project.
     *
     * @param obj The project for which tags are to be retrieved.
     * @return A list of ProjectTag objects associated with the project.
     */
    private List<ProjectTag> findTagList(Project obj)
    {
        return toStream(m_projectTagServiceHelper.getAllProjectTagByProjectId(obj.getProjectId())).toList();
    }

    /**
     * Handles the completion of a project by its owner and sends notifications to project participants.
     *
     * @param userId    The UUID of the user (project owner).
     * @param projectId The UUID of the project to be finished.
     * @return A ResponseMessage object containing the final project details.
     * @throws DataServiceException if the user is not the owner of the project.
     */
    private ResponseMessage<Object> finishProjectCallback(UUID userId, UUID projectId)
    {
        var user = findUserIfExists(userId);
        var project = findProjectIfExistsByProjectId(projectId);

        if (!project.getProjectOwner().getUserId().equals(user.getUserId()))
            throw new DataServiceException("You are not owner of this project!");

        project.finishProject();
        var savedProject = m_projectServiceHelper.saveProject(project);
        user.setOwnerProjectCount(user.getOwnerProjectCount() - 1);
        user.setTotalProjectCount(user.getTotalProjectCount() - 1);
        m_projectServiceHelper.addUser(user);
        var detailDTO = m_projectMapper.toProjectDetailDTO(savedProject, findTagList(savedProject), findProjectParticipantsByProjectId(savedProject));
        // Send notifications to participants
        project.getProjectParticipants().forEach(p -> {
            var message = format("Project Owner: %s finished %s project!", project.getProjectOwner().getFullName(), project.getProjectName());
            sendNotificationToUser(project, p.getUser(), project.getProjectOwner(), message);
        });
        return new ResponseMessage<>("Project is finished!", OK, detailDTO);
    }

    /**
     * Changes the status of a project and updates its details.
     *
     * @param userId        The UUID of the user (project owner).
     * @param projectId     The UUID of the project whose status is to be changed.
     * @param projectStatus The new status to set for the project.
     * @return A ResponseMessage object containing the updated project details.
     * @throws DataServiceException if the user is not the owner of the project.
     */
    private ResponseMessage<Object> changeProjectStatusCallback(UUID userId, UUID projectId, EProjectStatus projectStatus)
    {
        var user = findUserIfExists(userId);
        var project = findProjectIfExistsByProjectId(projectId);

        if (!project.getProjectOwner().getUserId().equals(user.getUserId()))
            throw new DataServiceException("You are not owner of this project!");

        project.setProjectStatus(projectStatus);
        var savedProject = m_projectServiceHelper.saveProject(project);

        var detailDTO = m_projectMapper.toProjectDetailDTO(savedProject, findTagList(savedProject), findProjectParticipantsByProjectId(savedProject));

        return new ResponseMessage<>(format("Project status changed to %s!", projectStatus), OK, detailDTO);
    }

    /**
     * Retrieves all participants of a specific project.
     *
     * @param obj The project for which participants are to be retrieved.
     * @return A ProjectsParticipantDTO containing the list of participants.
     */
    private ProjectsParticipantDTO findProjectParticipantsByProjectId(Project obj)
    {
        var participants = m_projectServiceHelper.findAllProjectParticipantByProjectId(obj.getProjectId());
        return m_projectParticipantMapper.toProjectsParticipantDTO(toList(participants, m_projectParticipantMapper::toProjectParticipantDTO));
    }

    /**
     * Retrieves a project by its unique identifier if it exists.
     *
     * @param projectId The UUID of the project to be retrieved.
     * @return The Project entity if found.
     * @throws DataServiceException if the project with the specified ID is not found.
     */
    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    /**
     * Retrieves a user by their unique identifier if they exist.
     *
     * @param userId The UUID of the user to be retrieved.
     * @return The User entity if found.
     * @throws DataServiceException if the user with the specified ID is not found.
     */
    private User findUserIfExists(UUID userId)
    {
        var user = m_projectServiceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", userId));

        return user.get();
    }

    /**
     * Removes a project.
     *
     * @param userId    The UUID of the user removing the project.
     * @param projectId The UUID of the project to be removed.
     * @return A ResponseMessage containing the result of the remove operation.
     */
    public ResponseMessage<Object> removeProjectCallback(UUID userId, UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var user = findUserIfExists(userId);

        if (!project.getProjectOwner().getUserId().equals(user.getUserId()))
            throw new DataServiceException("You are not owner of this project!");

       /* if (!project.getProjectParticipants().isEmpty())
        {

        }*/

        if (project.getStartDate().isAfter(LocalDate.now()))
        {
            project.getProjectParticipants().forEach(p -> {
                var message = format("%s removed you from %s project!", project.getProjectOwner().getFullName(), project.getProjectName());
                sendNotificationToUser(project, p.getUser(), project.getProjectOwner(), message);
            });
            m_projectServiceHelper.removeProjectById(projectId);
        }

        return null;
    }

}
