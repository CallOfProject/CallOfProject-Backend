package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.enums.NotificationType;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
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
     * Remove participant with given project id and user id.
     *
     * @param projectId represent the project id
     * @param userId    represent the user id
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> removeParticipant(UUID projectId, UUID userId)
    {
        return doForDataService(() -> removeParticipantCallback(projectId, userId), "ProjectService::removeParticipant");
    }


    /**
     * Approve or Reject Project Participant Request
     *
     * @return if success ProjectDTO else return Error Message
     */
    public ResponseMessage<Object> approveParticipantRequest(ParticipantRequestDTO requestDTO)
    {
        var result = doForDataService(() -> approveParticipantRequestCallback(requestDTO), "ProjectService::approveParticipantRequest");

        if (result.getStatusCode() == ACCEPTED && result.getObject() instanceof ParticipantStatusDTO dto && dto.isAccepted())
        {
            var message = format("%s accepted your request to join %s project!", dto.owner().getFullName(), dto.project().getProjectName());
            sendNotificationToUser(dto.project(), dto.user(), dto.owner(), message);
        }
        if (result.getStatusCode() == NOT_ACCEPTED && result.getObject() instanceof ParticipantStatusDTO dto && !dto.isAccepted())
        {
            var message = format("%s denied your request to join %s project!", dto.owner().getFullName(), dto.project().getProjectName());
            sendNotificationToUser(dto.project(), dto.user(), dto.owner(), message);
        }

        return new ResponseMessage<>(result.getMessage(), result.getStatusCode(), result.getStatusCode() == ACCEPTED);
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

    public ResponseMessage<Object> changeProjectStatus(UUID userId, UUID projectId, EProjectStatus projectStatus)
    {
        return doForDataService(() -> changeProjectStatusCallback(userId, projectId, projectStatus), "ProjectService::changeProjectStatus");
    }

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

    public ResponseMessage<Object> removeParticipantCallback(UUID projectId, UUID userId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var user = findUserIfExists(userId);

        var participant = project
                .getProjectParticipants()
                .stream()
                .filter(p -> p.getUser().getUserId().equals(user.getUserId()))
                .findFirst();

        if (participant.isEmpty())
            return new ResponseMessage<>("User is not participant of this project", OK, false);

        project.getProjectParticipants().remove(participant.get());
        m_projectServiceHelper.removeParticipant(participant.get().getProjectId());
        m_projectServiceHelper.saveProject(project);
        m_projectServiceHelper.addUser(user);

        var message = format("%s removed you from %s project!", project.getProjectOwner().getFullName(), project.getProjectName());

        return new ResponseMessage<>(message, OK, true);
    }

    private ResponseMessage<Object> deniedParticipantRequest(User user, Project project, User projectOwner)
    {
        return new ResponseMessage<>("Participant request is not accepted!", NOT_ACCEPTED, new ParticipantStatusDTO(project, user, projectOwner, false));
    }

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

    private void sendNotificationToUser(Project project, User user, User owner, String message)
    {
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
        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage), "ProjectService::approveParticipantRequest");
    }

   /* private void sendFailNotificationToUser(Project project, User user, User owner)
    {
        var msg = format("%s denied your request to join %s project!", owner.getFullName(), project.getProjectName());

        var data = new NotificationObject(project.getProjectId(), user.getUserId());

        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

        // Project owner to user message
        var notificationMessage = new ProjectParticipantRequestDTO.Builder()
                .setFromUserId(owner.getUserId())
                .setToUserId(user.getUserId())
                .setMessage(msg)
                .setNotificationType(NotificationType.INFORMATION)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .build();

        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage), "ProjectService::approveParticipantRequest");
    }*/

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
