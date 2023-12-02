package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.dal.TagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.entity.Tag;
import callofproject.dev.nosql.enums.NotificationType;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.util.Policy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static callofproject.dev.util.stream.StreamUtil.toStreamConcurrent;
import static java.lang.String.format;

@Service
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final TagServiceHelper m_tagServiceHelper;
    private final IProjectMapper m_projectMapper;
    private final KafkaProducer m_kafkaProducer;
    private final ObjectMapper m_objectMapper;

    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper,
                          @Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper projectTagServiceHelper,
                          @Qualifier(TAG_SERVICE_HELPER_BEAN_NAME) TagServiceHelper tagServiceHelper,
                          IProjectMapper projectMapper, KafkaProducer kafkaProducer, ObjectMapper objectMapper)
    {
        m_serviceHelper = serviceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectMapper = projectMapper;
        m_kafkaProducer = kafkaProducer;
        m_objectMapper = objectMapper;
    }


    /**
     * Save Project with given dto class.
     *
     * @param projectDTO represent the dto class
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> saveProject(ProjectSaveDTO projectDTO)
    {
        return doForDataService(() -> saveProjectCallback(projectDTO), "ProjectService::saveProject");
    }

    /**
     * Save Project with given dto class.
     *
     * @param projectDTO represent the dto class
     * @return ProjectOverviewDTO.
     */
    public ResponseMessage<Object> saveProjectCallback(ProjectSaveDTO projectDTO)
    {
        var user = m_serviceHelper.findUserById(projectDTO.userId());

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", projectDTO.userId()));

        if (user.get().getOwnerProjectCount() >= Policy.OWNER_MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are owner of %d projects already!", Policy.OWNER_MAX_PROJECT_COUNT),
                    Status.NOT_ACCEPTED, false);

        if (user.get().getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    Status.NOT_ACCEPTED, false);

        var projectAccessType = m_serviceHelper.findProjectAccessTypeByProjectAccessType(projectDTO.projectAccessType());
        var projectLevelType = m_serviceHelper.findProjectLevelByProjectLevel(projectDTO.projectLevel());
        var professionLevelType = m_serviceHelper.findProjectProfessionLevelByProjectProfessionLevel(projectDTO.professionLevel());
        var sectorType = m_serviceHelper.findSectorBySector(projectDTO.sector());
        var degreeType = m_serviceHelper.findDegreeByDegree(projectDTO.degree());
        var interviewTypeType = m_serviceHelper.findInterviewTypeByInterviewType(projectDTO.interviewType());

        if (projectAccessType.isEmpty() || projectLevelType.isEmpty() || professionLevelType.isEmpty() || sectorType.isEmpty() || degreeType.isEmpty() || interviewTypeType.isEmpty())
            throw new DataServiceException("Project Access Type or Project Level or Profession Level or Sector or Degree or Interview Type is not found!");

        var project = new Project.Builder()
                .setStartDate(projectDTO.startDate())
                .setFeedbackTimeRange(projectDTO.feedbackTimeRange())
                .setProjectAccessType(projectAccessType.get())
                .setProjectOwner(user.get())
                .setProjectLevel(projectLevelType.get())
                .setProfessionLevel(professionLevelType.get())
                .setSector(sectorType.get())
                .setDegree(degreeType.get())
                .setInterviewType(interviewTypeType.get())
                .setProjectAim(projectDTO.projectAim())
                .setDescription(projectDTO.projectDescription())
                .setExpectedCompletionDate(projectDTO.expectedCompletionDate())
                .setApplicationDeadline(projectDTO.applicationDeadline())
                .setProjectImagePath(projectDTO.projectImage())
                .setProjectName(projectDTO.projectName())
                .setProjectSummary(projectDTO.projectSummary())
                .setSpecialRequirements(projectDTO.specialRequirements())
                .setTechnicalRequirements(projectDTO.technicalRequirements())
                .setMaxParticipant(projectDTO.maxParticipantCount())
                .build();

        var savedProject = m_serviceHelper.saveProject(project);

        saveTagsIfNotExists(projectDTO.tags(), savedProject);
        var tagList = toStream(m_projectTagServiceHelper.getAllProjectTagByProjectId(savedProject.getProjectId())).toList();

        user.get().setOwnerProjectCount(user.get().getOwnerProjectCount() + 1);
        user.get().setTotalProjectCount(user.get().getTotalProjectCount() + 1);
        m_serviceHelper.addUser(user.get());

        return new ResponseMessage<>("Project Created Successfully!", Status.CREATED, m_projectMapper.toProjectOverviewDTO(savedProject, tagList));
    }

    /**
     * Save tags if not exists.
     *
     * @param tags         represent the tags.
     * @param savedProject represent the saved project.
     */
    private void saveTagsIfNotExists(List<String> tags, Project savedProject)
    {
        for (var tag : tags)
        {
            if (m_tagServiceHelper.existsByTagNameContainsIgnoreCase(tag.replace(" ", ""))) // If tag already exists then save project tag
                m_projectTagServiceHelper.saveProjectTag(new ProjectTag(tag, savedProject.getProjectId()));

            else // If tag not exists then save tag and project tag
            {
                m_tagServiceHelper.saveTag(new Tag(tag));
                m_projectTagServiceHelper.saveProjectTag(new ProjectTag(tag, savedProject.getProjectId()));
            }
        }

        // If project tags not contains string tags, then remove project tag.
        //var projectTags = toStreamConcurrent(m_projectTagServiceHelper.getAllProjectTagByProjectId(savedProject.getProjectId())).toList();

        //projectTags.stream().filter(projectTag -> !tags.contains(projectTag.getTagName())).forEach(m_projectTagServiceHelper::removeProjectTag);
    }

    /**
     * Find project by id.
     *
     * @param userId represent the user id
     * @return ProjectDTO class.
     */

    public ResponseMessage<Object> findProjectsByUserId(String userId)
    {
        var projects = doForDataService(() -> findProjectsByUserId(userId), "ProjectService::findProjectsByUserId");

        return new ResponseMessage<>("Projects found!", Status.OK, projects);
    }


    public MultipleResponseMessagePageable<Object> findAll(int page)
    {
        var projectPageable = m_serviceHelper.findAllProjectsPageable(page);
        var projects = toStreamConcurrent(projectPageable).toList();
        var totalPage = projectPageable.getTotalPages();

        if (projects.isEmpty())
            return new MultipleResponseMessagePageable<>(totalPage, page, 0, "Projects not found!", null);

        var projectOverviewList = new ArrayList<ProjectOverviewDTO>();

        for (var project : projects)
        {
            var tags = toStreamConcurrent(m_projectTagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();
            projectOverviewList.add(m_projectMapper.toProjectOverviewDTO(project, tags));
        }

        var projectWithParticipants = doForDataService(() -> m_projectMapper.toProjectOverviewsDTO(projectOverviewList), "ProjectService::findAllParticipantProjectByUserId");

        return new MultipleResponseMessagePageable<>(totalPage, page, projectOverviewList.size(), "Projects found!", projectWithParticipants);
    }


    /**
     * Add participant with given project id and user id.
     *
     * @param dto represent the SaveProjectParticipantDTO class
     * @return true if success else false.
     */
    public boolean addParticipant(SaveProjectParticipantDTO dto)
    {
        return doForDataService(() -> m_serviceHelper.addProjectParticipant(dto.project_id(), dto.user_id()),
                "ProjectService::addParticipant");
    }


    /**
     * Add project join request with given project id and user id.
     *
     * @param projectId represent the project id
     * @param userId    represent the user id
     * @return true if success else false.
     */
    public ResponseMessage<Object> addProjectJoinRequest(UUID projectId, UUID userId)
    {

        var user = m_serviceHelper.findUserById(userId);
        var project = m_serviceHelper.findProjectById(projectId);

        if (user.isEmpty() || project.isEmpty())
            throw new DataServiceException(format("User with id: %s or Project with id: %s is not found!", userId, projectId));

        if (user.get().getParticipantProjectCount() >= Policy.MAX_PARTICIPANT_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are participant %d projects already!", Policy.MAX_PARTICIPANT_PROJECT_COUNT),
                    Status.NOT_ACCEPTED, false);


        if (user.get().getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    Status.NOT_ACCEPTED, false);

        var result = doForDataService(() -> m_serviceHelper.sendParticipantRequestToProject(projectId, userId),
                "ProjectService::addProjectJoinRequest");

        if (!result)
            return new ResponseMessage<>("You are participant of project already! ", Status.NOT_ACCEPTED, false);

        // Send notification to project owner (Approve Message)
        sendNotificationToProjectOwner(userId, projectId);

        return new ResponseMessage<>("Participant request is sent!", Status.OK, true);
    }

    private void sendNotificationToProjectOwner(UUID userId, UUID projectId)
    {
        var user = m_serviceHelper.findUserById(userId);
        var project = m_serviceHelper.findProjectById(projectId);

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

    /**
     * Find all project by user id who is participant in project.
     *
     * @param userId represent the user id
     * @param page   represent the page
     * @return ProjectOverviewsDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllParticipantProjectByUserId(UUID userId, int page)
    {
        var projectPageable = m_serviceHelper.findAllParticipantProjectByUserId(userId, page);
        var projects = toStreamConcurrent(projectPageable).toList();
        var totalPage = projectPageable.getTotalPages();

        if (projects.isEmpty())
            throw new DataServiceException(format("User with id: %s is not participant in any project!", userId));

        var projectOverviewList = new ArrayList<ProjectOverviewDTO>();

        for (var project : projects)
        {
            var tags = toStreamConcurrent(m_projectTagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();
            projectOverviewList.add(m_projectMapper.toProjectOverviewDTO(project, tags));
        }

        var projectWithParticipants = doForDataService(() -> m_projectMapper.toProjectOverviewsDTO(projectOverviewList), "ProjectService::findAllParticipantProjectByUserId");

        return new MultipleResponseMessagePageable<>(totalPage, page, projectOverviewList.size(), "Projects found!", projectWithParticipants);
    }


    private Project findProjectById(UUID projectId)
    {
        var project = doForDataService(() -> m_serviceHelper.findProjectById(projectId), "ProjectService::findProjectById");

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    private ResponseMessage<Object> findUserById(UUID userId)
    {
        var user = doForDataService(() -> m_serviceHelper.findUserById(userId), "ProjectService::findUserById");

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", userId));

        return new ResponseMessage<>("User created successfully!", Status.OK, user.get());
    }

    private ProjectParticipantRequest findProjectParticipantRequestByRequestId(UUID requestId)
    {
        var request = doForDataService(() -> m_serviceHelper.findProjectParticipantRequestByParticipantRequestId(requestId),
                "ProjectService::findProjectParticipantRequestByRequestId");

        if (request.isEmpty())
            throw new DataServiceException("Participant request is not found!");

        if (request.get().isAccepted())
            throw new DataServiceException("Participant request is already accepted!");

        return request.get();
    }


    /**
     * Find all project by user id who is owner in project.
     *
     * @param requestDTO represent the requestDTO class
     * @return ProjectOverviewsDTO class.
     */
    public ResponseMessage<Object> approveParticipantRequest(ParticipantRequestDTO requestDTO)
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
            m_serviceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId());
            return new ResponseMessage<>("User is already participant in this project!", Status.NOT_ACCEPTED, false);
        }

        if (!requestDTO.isAccepted()) // if not  accepted then call denied
            return deniedParticipantRequest(participantRequest, user, project, projectOwner);

        return approveParticipant(participantRequest, user, project, projectOwner);
    }


    /**
     * Denies participant request.
     *
     * @param participantRequest represent the participant request.
     * @param user               represent the user.
     * @param project            represent the project.
     * @param projectOwner       represent the project owner.
     * @return ResponseDTO class.
     */
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

        m_serviceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId());

        return new ResponseMessage<>("Participant request is not accepted!", Status.NOT_ACCEPTED, false);
    }

    /**
     * Approve participant request.
     *
     * @param participantRequest represent the participant request.
     * @param user               represent the user.
     * @param project            represent the project.
     * @param owner              represent the owner.
     * @return ResponseDTO class.
     */
    private ResponseMessage<Object> approveParticipant(ProjectParticipantRequest participantRequest, User user, Project project, User owner)
    {
        // Add participant to project
        addParticipant(new SaveProjectParticipantDTO(user.getUserId(), project.getProjectId()));

        // Remove participant request
        doForDataService(() -> m_serviceHelper.removeParticipantRequestByRequestId(participantRequest.getParticipantRequestId()),
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
        m_serviceHelper.addUser(user);

        // Send notification to user
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage), "ProjectService::approveParticipantRequest");

        return new ResponseMessage<>("Participant request is accepted!", Status.ACCEPTED, true);
    }

    public ResponseMessage<Object> updateProject(ProjectUpdateDTO projectDTO)
    {
        var project = findProjectById(projectDTO.projectId());

        if (!project.getProjectOwner().getUserId().equals(projectDTO.userId()))
            throw new DataServiceException("You are not owner of this project!");

        var projectAccessType = m_serviceHelper.findProjectAccessTypeByProjectAccessType(projectDTO.projectAccessType());
        var projectLevelType = m_serviceHelper.findProjectLevelByProjectLevel(projectDTO.projectLevel());
        var professionLevelType = m_serviceHelper.findProjectProfessionLevelByProjectProfessionLevel(projectDTO.professionLevel());
        var sectorType = m_serviceHelper.findSectorBySector(projectDTO.sector());
        var degreeType = m_serviceHelper.findDegreeByDegree(projectDTO.degree());
        var interviewTypeType = m_serviceHelper.findInterviewTypeByInterviewType(projectDTO.interviewType());

        if (projectAccessType.isEmpty() || projectLevelType.isEmpty() || professionLevelType.isEmpty() || sectorType.isEmpty() || degreeType.isEmpty() || interviewTypeType.isEmpty())
            throw new DataServiceException("Project Access Type or Project Level or Profession Level or Sector or Degree or Interview Type is not found!");

        project.setStartDate(projectDTO.startDate());
        project.setFeedbackTimeRange(projectDTO.feedbackTimeRange());
        project.setProjectAccessType(projectAccessType.get());
        project.setProjectLevel(projectLevelType.get());
        project.setProfessionLevel(professionLevelType.get());
        project.setSector(sectorType.get());
        project.setDegree(degreeType.get());
        project.setInterviewType(interviewTypeType.get());
        project.setProjectAim(projectDTO.projectAim());
        project.setDescription(projectDTO.projectDescription());
        project.setExpectedCompletionDate(projectDTO.expectedCompletionDate());
        project.setApplicationDeadline(projectDTO.applicationDeadline());
        project.setProjectImagePath(projectDTO.projectImage());
        project.setProjectName(projectDTO.projectName());
        project.setProjectSummary(projectDTO.projectSummary());
        project.setSpecialRequirements(projectDTO.specialRequirements());
        project.setTechnicalRequirements(projectDTO.technicalRequirements());
        project.setMaxParticipant(projectDTO.maxParticipantCount());

        m_serviceHelper.saveProject(project);
        // Save new tags to db.
        saveTagsIfNotExists(projectDTO.tags(), project);
        // Mevcut projenin tagları
        var tagList = toStream(m_projectTagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();

        var overviewDTO = m_projectMapper.toProjectOverviewDTO(project, tagList);

        return new ResponseMessage<>("Project is updated!", Status.OK, overviewDTO);
    }
}
