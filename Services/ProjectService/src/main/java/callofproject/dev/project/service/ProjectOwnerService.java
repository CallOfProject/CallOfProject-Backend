package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.enums.NotificationType;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import callofproject.dev.project.util.Policy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.common.status.Status.*;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toList;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static java.lang.String.format;

@Service
@Lazy
public class ProjectOwnerService
{
    private final ProjectServiceHelper m_projectServiceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final ObjectMapper m_objectMapper;
    private final KafkaProducer m_kafkaProducer;
    private final IProjectMapper m_projectMapper;
    private final IProjectParticipantMapper m_projectParticipantMapper;

    public ProjectOwnerService(ProjectServiceHelper projectServiceHelper, ProjectTagServiceHelper projectTagServiceHelper, ObjectMapper objectMapper, KafkaProducer kafkaProducer, IProjectMapper projectMapper, IProjectParticipantMapper projectParticipantMapper)
    {
        m_projectServiceHelper = projectServiceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_objectMapper = objectMapper;
        m_kafkaProducer = kafkaProducer;
        m_projectMapper = projectMapper;
        m_projectParticipantMapper = projectParticipantMapper;
    }

    /**
     * Add project join request with given project id and user id.
     *
     * @param projectId represent the project id
     * @param userId    represent the user id
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> addProjectJoinRequest(UUID projectId, UUID userId)
    {
        return doForDataService(() -> addProjectJoinRequestCallback(projectId, userId), "ProjectService::addProjectJoinRequest");
    }

    /**
     * Add participant with given project id and user id.
     *
     * @param dto represent the SaveProjectParticipantDTO class
     * @return true if success else false.
     */
    public boolean addParticipant(SaveProjectParticipantDTO dto)
    {
        return doForDataService(() -> m_projectServiceHelper.addProjectParticipant(dto.project_id(), dto.user_id()),
                "ProjectService::addParticipant");
    }

    /**
     * Approve or Reject Project Participant Request
     *
     * @return if success ProjectDTO else return Error Message
     */
    public ResponseMessage<Object> approveParticipantRequest(ParticipantRequestDTO requestDTO)
    {
        return doForDataService(() -> approveParticipantRequestCallback(requestDTO), "ProjectService::approveParticipantRequest");
    }

    /**
     * Finish project with given project id.
     *
     * @param userId    represent the user id
     * @param projectId represent the project id
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> finishProject(UUID userId, UUID projectId)
    {
        return doForDataService(() -> finishProjectCallback(userId, projectId), "ProjectService::finishProject");
    }

    private ResponseMessage<Object> approveParticipantRequestCallback(ParticipantRequestDTO requestDTO)
    {
        var participantRequest = findProjectParticipantRequestByRequestId(requestDTO.requestId());
        var project = participantRequest.getProject();
        var user = participantRequest.getUser();
        var projectOwner = project.getProjectOwner();

        var isExistsUser = project.getProjectParticipants()
                .stream()
                .anyMatch(p -> p.getUser().getUserId().equals(user.getUserId()) && p.getProject().getProjectId().equals(project.getProjectId()));
        if (isExistsUser)
        {
            m_projectServiceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId());
            return new ResponseMessage<>("User is already participant in this project!", NOT_ACCEPTED, false);
        }

        if (!requestDTO.isAccepted()) // if not  accepted then call denied
            return deniedParticipantRequest(participantRequest, user, project, projectOwner);

        return approveParticipant(participantRequest, user, project, projectOwner);
    }

    private ResponseMessage<Object> deniedParticipantRequest(ProjectParticipantRequest participantRequest, User user, Project project, User projectOwner)
    {
        var msg = format("%s denied your request to join %s project!", projectOwner.getFullName(), project.getProjectName());

        var data = new NotificationObject(project.getProjectId(), user.getUserId());

        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

        // Project owner to user message
        var notificationMessage = new ProjectParticipantRequestDTO.Builder()
                .setFromUserId(projectOwner.getUserId())
                .setToUserId(user.getUserId())
                .setMessage(msg)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .build();

        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage), "ProjectService::approveParticipantRequest");

        m_projectServiceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId());

        return new ResponseMessage<>("Participant request is not accepted!", NOT_ACCEPTED, false);
    }

    private ResponseMessage<Object> approveParticipant(ProjectParticipantRequest participantRequest, User user, Project project, User owner)
    {
        // Add participant to project
        addParticipant(new SaveProjectParticipantDTO(user.getUserId(), project.getProjectId()));

        // Remove participant request
        doForDataService(() -> m_projectServiceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId()),
                "ProjectService::approveParticipant::removeParticipantRequestByRequestId");

        var message = format("%s accepted your request to join %s project!", owner.getFullName(), project.getProjectName());

        // Project owner to user message
        var data = new NotificationObject(project.getProjectId(), user.getUserId());
        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

        var notificationMessage = new ProjectParticipantRequestDTO.Builder()
                .setFromUserId(owner.getUserId())
                .setToUserId(user.getUserId())
                .setMessage(message)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .build();

        // update user
        user.setParticipantProjectCount(user.getParticipantProjectCount() + 1);
        user.setTotalProjectCount(user.getTotalProjectCount() + 1);
        m_projectServiceHelper.addUser(user);

        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage), "ProjectService::approveParticipantRequest");

        return new ResponseMessage<>("Participant request is accepted!", ACCEPTED, true);
    }

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

    private ResponseMessage<Object> addProjectJoinRequestCallback(UUID projectId, UUID userId)
    {
        var user = m_projectServiceHelper.findUserById(userId);
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (user.isEmpty() || project.isEmpty())
            throw new DataServiceException(format("User with id: %s or Project with id: %s is not found!", userId, projectId));

        if (user.get().getParticipantProjectCount() >= Policy.MAX_PARTICIPANT_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are participant %d projects already!", Policy.MAX_PARTICIPANT_PROJECT_COUNT),
                    NOT_ACCEPTED, false);


        if (user.get().getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

        var result = doForDataService(() -> m_projectServiceHelper.sendParticipantRequestToProject(projectId, userId),
                "ProjectService::addProjectJoinRequest");

        if (!result)
            return new ResponseMessage<>("You are participant of project already! ", NOT_ACCEPTED, false);

        // Send notification to project owner (Approve Message)
        sendNotificationToProjectOwner(userId, projectId);

        return new ResponseMessage<>("Participant request is sent!", OK, true);
    }

    private void sendNotificationToProjectOwner(UUID userId, UUID projectId)
    {
        var user = m_projectServiceHelper.findUserById(userId);
        var project = m_projectServiceHelper.findProjectById(projectId);

        if (user.isEmpty() || project.isEmpty())
            throw new DataServiceException(format("User with id: %s or Project with id: %s is not found!", userId, projectId));

        var msg = format("%s wants to join your %s project!", user.get().getUsername(), project.get().getProjectName());

        // Create notification data
        var data = new NotificationObject(project.get().getProjectId(), user.get().getUserId());

        // Convert data to json.
        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

        // Project owner to user message
        var notificationMessage = new ProjectParticipantRequestDTO.Builder()
                .setFromUserId(user.get().getUserId())
                .setToUserId(project.get().getProjectOwner().getUserId())
                .setMessage(msg)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .build();

        // Send notification to project owner
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage),
                "ProjectService::sendNotificationToProjectOwner");
    }

    private List<ProjectTag> findTagList(Project obj)
    {
        return toStream(m_projectTagServiceHelper.getAllProjectTagByProjectId(obj.getProjectId())).toList();
    }

    private ResponseMessage<Object> finishProjectCallback(UUID userId, UUID projectId)
    {
        var user = findUserIfExists(userId);
        var project = findProjectIfExistsByProjectId(projectId);

        if (!project.getProjectOwner().getUserId().equals(user.getUserId()))
            throw new DataServiceException("You are not owner of this project!");

        project.finishProject();
        var savedProject = m_projectServiceHelper.saveProject(project);

        var detailDTO = m_projectMapper.toProjectDetailDTO(savedProject, findTagList(savedProject), findProjectParticipantsByProjectId(savedProject));

        return new ResponseMessage<>("Project is finished!", OK, detailDTO);
    }

    private ProjectsParticipantDTO findProjectParticipantsByProjectId(Project obj)
    {
        var participants = m_projectServiceHelper.findAllProjectParticipantByProjectId(obj.getProjectId());
        return m_projectParticipantMapper.toProjectsParticipantDTO(toList(participants, m_projectParticipantMapper::toProjectParticipantDTO));
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
}
