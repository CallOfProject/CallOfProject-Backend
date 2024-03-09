package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.AdminOperationStatus;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.dal.TagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.entity.Tag;
import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;
import callofproject.dev.nosql.repository.IProjectTagRepository;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.project.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.project.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import callofproject.dev.project.mapper.IUserMapper;
import callofproject.dev.project.util.Policy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static callofproject.dev.data.common.status.Status.*;
import static callofproject.dev.data.common.util.UtilityMethod.convert;
import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.project.util.Constants.PROJECT_SERVICE;
import static callofproject.dev.util.stream.StreamUtil.*;
import static java.lang.String.format;

/**
 * Service class for managing projects.
 * It implements the IProjectService interface.
 */
@Service(PROJECT_SERVICE)
@Lazy
public class ProjectService implements IProjectService
{
    private final ProjectServiceHelper m_serviceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final TagServiceHelper m_tagServiceHelper;
    private final KafkaProducer m_kafkaProducer;
    private final ObjectMapper m_objectMapper;
    private final IProjectMapper m_projectMapper;
    private final S3Service m_s3Service;
    private final IProjectParticipantMapper m_projectParticipantMapper;
    private final IProjectTagRepository m_projectTagRepository;
    private final IUserMapper m_userMapper;

    @Value("${notification.request.approve}")
    private String m_approvalLink;

    /**
     * Constructor for ProjectService.
     *
     * @param serviceHelper            The ProjectServiceHelper to be used for data access.
     * @param projectTagServiceHelper  The ProjectTagServiceHelper to be used for data access.
     * @param tagServiceHelper         The TagServiceHelper to be used for data access.
     * @param kafkaProducer            The KafkaProducer to be used for sending notifications.
     * @param objectMapper             The ObjectMapper to be used for converting objects to JSON.
     * @param projectMapper            The IProjectMapper to be used for mapping Project entities to DTOs.
     * @param s3Service                The S3Service to be used for uploading files to S3.
     * @param projectParticipantMapper The IProjectParticipantMapper to be used for mapping ProjectParticipant entities to DTOs.
     * @param projectTagRepository     The IProjectTagRepository to be used for data access.
     */
    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper,
                          @Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper projectTagServiceHelper,
                          @Qualifier(TAG_SERVICE_HELPER_BEAN_NAME) TagServiceHelper tagServiceHelper, KafkaProducer kafkaProducer, ObjectMapper objectMapper,
                          IProjectMapper projectMapper, S3Service s3Service, IProjectParticipantMapper projectParticipantMapper, IProjectTagRepository projectTagRepository, IUserMapper userMapper)
    {
        m_serviceHelper = serviceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_kafkaProducer = kafkaProducer;
        m_objectMapper = objectMapper;
        m_projectMapper = projectMapper;
        m_s3Service = s3Service;
        m_projectParticipantMapper = projectParticipantMapper;
        m_projectTagRepository = projectTagRepository;
        m_userMapper = userMapper;
    }


    /**
     * Saves a new project using a ProjectSaveDTO.
     *
     * @param projectDTO Data Transfer Object containing project information.
     * @return A ResponseMessage containing the result of the save operation.
     */
    @Override
    public ResponseMessage<Object> saveProject(ProjectSaveDTO projectDTO)
    {
        return doForDataService(() -> saveProjectCallback(projectDTO), "ProjectService::saveProject");
    }

    /**
     * Saves a new project with an additional file using ProjectSaveDTO and MultipartFile.
     *
     * @param saveDTO Data Transfer Object containing project information.
     * @param file    The file to be associated with the project.
     * @return A ResponseMessage containing the result of the save operation.
     */
    @Override
    public ResponseMessage<Object> saveProjectV2(ProjectSaveDTO saveDTO, MultipartFile file)
    {
        var savedProject = doForDataService(() -> saveProjectCallbackV2(saveDTO, file), "ProjectService::saveProject");

        if (savedProject.getStatusCode() == CREATED)
        {
            var projectOverviewDTO = (ProjectOverviewDTO) savedProject.getObject();
            var project = findProjectIfExistsByProjectId(UUID.fromString(projectOverviewDTO.projectId()));
            List<ProjectParticipantKafkaDTO> participants;

            if (project.getProjectParticipants() != null)
                participants = project.getProjectParticipants().stream().map(this::toProjectParticipantKafkaDTO).toList();
            else participants = List.of();

            var userOwner = findUserIfExists(projectOverviewDTO.projectOwnerName());
            var owner = m_userMapper.toUserKafkaDTO(userOwner);
            var projectKafkaDTO = new ProjectInfoKafkaDTO(project.getProjectId(), projectOverviewDTO.projectTitle(), owner, participants,
                    projectOverviewDTO.projectStatus(), project.getAdminOperationStatus());

            m_kafkaProducer.sendProjectInfo(projectKafkaDTO);
        }
        return savedProject;
    }

    private ProjectParticipantKafkaDTO toProjectParticipantKafkaDTO(ProjectParticipant participant)
    {
        return new ProjectParticipantKafkaDTO(participant.getProjectId(), participant.getProject().getProjectId(),
                participant.getUser().getUserId(), participant.getJoinDate(), false);
    }


    private UserKafkaDTO createUserKafkaDTO(User user)
    {
        return m_userMapper.toUserKafkaDTO(user);
    }

    /**
     * Retrieves all projects a user is participating in, with pagination.
     *
     * @param userId The UUID of the user.
     * @param page   The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of projects.
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAllParticipantProjectByUserId(UUID userId, int page)
    {
        return doForDataService(() -> findAllParticipantProjectByUserIdCallback(userId, page), "ProjectService::findAllParticipantProjectByUserId");
    }


    /**
     * Updates a project using ProjectUpdateDTO.
     *
     * @param projectDTO Data Transfer Object containing updated project information.
     * @return A ResponseMessage containing the result of the update operation.
     */
    @Override
    public ResponseMessage<Object> updateProject(ProjectUpdateDTO projectDTO)
    {
        return doForDataService(() -> updateProjectCallback(projectDTO), "ProjectService::updateProject");
    }


    /**
     * Updates a project with an additional file using ProjectUpdateDTO and MultipartFile.
     *
     * @param projectDTO Data Transfer Object containing updated project information.
     * @param file       The file to be associated with the project update.
     * @return A ResponseMessage containing the result of the update operation.
     */
    @Override
    public ResponseMessage<Object> updateProjectV2(ProjectUpdateDTO projectDTO, MultipartFile file)
    {
        return doForDataService(() -> updateProjectCallbackV2(projectDTO, file), "ProjectService::updateProject");
    }


    /**
     * Retrieves all projects owned by a user, with pagination.
     *
     * @param userId The UUID of the user.
     * @param page   The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of owned projects.
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserId(UUID userId, int page)
    {
        return doForDataService(() -> findAllOwnerProjectsByUserIdCallback(userId, page), "ProjectService::findAllOwnerProjectsByUserId");
    }

    /**
     * Retrieves all projects owned by a user, identified by username, with pagination.
     *
     * @param username The username of the project owner.
     * @param page     The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of owned projects.
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsername(String username, int page)
    {
        return doForDataService(() -> findAllOwnerProjectsByUsernameCallback(username, page), "ProjectService::findAllOwnerProjectsByUsername");
    }


    /**
     * Retrieves a view of a project from the perspective of its owner.
     *
     * @param userId    The UUID of the owner.
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectOwnerView.
     */
    @Override
    public ResponseMessage<Object> findProjectOwnerView(UUID userId, UUID projectId)
    {
        return doForDataService(() -> findProjectOwnerViewCallback(userId, projectId), "ProjectService::findProjectOwnerView");
    }

    /**
     * Retrieves a paginated list of projects for discovery purposes.
     *
     * @param page The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of project discovery views.
     */
    @Override
    public MultipleResponseMessagePageable<Object> findAllProjectDiscoveryView(int page)
    {
        return doForDataService(() -> findAllProjectDiscoveryViewCallback(page), "ProjectService::findAllProjectDiscoveryView");
    }

    /**
     * Retrieves an overview of a specific project.
     *
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectOverview.
     */
    @Override
    public ResponseMessage<Object> findProjectOverview(UUID projectId)
    {
        return doForDataService(() -> findProjectOverviewCallback(projectId), "ProjectService::findProjectOverview");
    }

    /**
     * Adds a request to join a project.
     *
     * @param projectId The UUID of the project to join.
     * @param userId    The UUID of the user requesting to join.
     * @return A ResponseMessage containing the result of the join request.
     */
    @Override
    public ResponseMessage<Object> addProjectJoinRequest(UUID projectId, UUID userId)
    {
        var result = doForDataService(() -> addProjectJoinRequestCallback(projectId, userId),
                "ProjectService::addProjectJoinRequest");

        var request = (ProjectParticipantRequestDTO) result.getObject();

        if (result.getStatusCode() == OK)
            sendNotificationToProjectOwner(request);

        return result;
    }

    /**
     * Retrieves detailed information of a specific project.
     *
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectDetail.
     */
    @Override
    public ResponseMessage<Object> findProjectDetail(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var tags = findTagList(project);

      /*  var img = m_s3Service.getImage(project.getProjectImagePath());
        project.setProjectImagePath(img);*/
        var projectDetailDTO = m_projectMapper.toProjectDetailDTO(project, tags, findProjectParticipantsDTOByProjectId(project));

        return new ResponseMessage<>("Project is found!", OK, projectDetailDTO);
    }

    /**
     * Retrieves detailed information of a specific project if the user has permission.
     *
     * @param projectId The UUID of the project.
     * @param userId    The UUID of the user.
     * @return A ResponseMessage containing the ProjectDetail.
     */
    @Override
    public ResponseMessage<Object> findProjectDetailIfHasPermission(UUID projectId, UUID userId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var user = findUserIfExists(userId);

        var isOwner = project.getProjectOwner().getUserId().equals(userId);
        var isParticipant = project.getProjectParticipants().stream().map(ProjectParticipant::getUser)
                .map(User::getUserId).anyMatch(user.getUserId()::equals);

        if (!isOwner && !isParticipant)
            return new ResponseMessage<>("User has not view permission", BAD_REQUEST, false);

        var tags = findTagList(project);

        var projectDetailDTO = m_projectMapper.toProjectDetailDTO(project, tags, findProjectParticipantsDTOByProjectId(project));

        return new ResponseMessage<>("Project is found!", OK, projectDetailDTO);
    }

    //-----------------------------------CALLBACKS---------------------------------------------

    /**
     * Callback for adding a request to join a project.
     *
     * @param projectId The UUID of the project to join.
     * @param userId    The UUID of the user requesting to join.
     * @return A ResponseMessage containing the result of the join request callback.
     */
    @Override
    public ResponseMessage<Object> addProjectJoinRequestCallback(UUID projectId, UUID userId)
    {
        var user = m_serviceHelper.findUserById(userId);
        var project = m_serviceHelper.findProjectById(projectId);

        // If user or project not exists then throw exception.
        if (user.isEmpty() || project.isEmpty())
            throw new DataServiceException(format("User with id: %s or Project with id: %s is not found!", userId, projectId));

        var projectChecked = checkProjectPolicies(user.get(), project.get());

        // If project checked is present then return error message.
        if (projectChecked.getStatusCode() != OK)
            return projectChecked;

        var result = doForDataService(() -> m_serviceHelper.sendParticipantRequestToProject(new ProjectParticipantRequest(project.get(), user.get())), "ProjectService::addProjectJoinRequest");

        var dto = new ProjectParticipantRequestDTO(userId, projectId, result.getParticipantRequestId());
        return new ResponseMessage<>("Participant request is sent!", OK, dto);
    }

    /**
     * Checks various project participation policies against a specific user and project.
     * Validates if the user is eligible to join or interact with the project based on predefined rules.
     *
     * @param user    The user for whom policy validation is being performed.
     * @param project The project to be checked against the user.
     * @return A ResponseMessage object indicating the result of the policy check.
     */
    private ResponseMessage<Object> checkProjectPolicies(User user, Project project)
    {
        // If user is owner of project then return error message.
        if (user.getProjects().stream().map(Project::getProjectId).anyMatch(project.getProjectId()::equals))
            return new ResponseMessage<>("You are owner of project!", NOT_ACCEPTED, null);

        // If user is already joined to project then return error message.
        if (user.getProjectParticipants().stream().map(ProjectParticipant::getProjectId).anyMatch(project.getProjectId()::equals))
            return new ResponseMessage<>("you are participant of project already!", NOT_ACCEPTED, null);

        // If user sent request already then return error message.
        if (user.getProjectParticipantRequests().stream().map(p -> p.getProject().getProjectId()).anyMatch(project.getProjectId()::equals))
            return new ResponseMessage<>("you sent request already!", NOT_ACCEPTED, null);

        // If user participant project count is greater than or equals to max participant project count then return error message.
        if (user.getParticipantProjectCount() >= Policy.MAX_PARTICIPANT_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are participant %d projects already!", Policy.MAX_PARTICIPANT_PROJECT_COUNT),
                    NOT_ACCEPTED, null);

        // If user total project count is greater than or equals to max project count then return error message.
        if (user.getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, null);

        return new ResponseMessage<>("", OK, null);
    }

    /**
     * Sends a notification to the owner of a project about a participant request.
     *
     * @param request DTO containing details of the project participant request.
     */
    private void sendNotificationToProjectOwner(ProjectParticipantRequestDTO request)
    {
        var user = findUserIfExists(request.userId());
        var project = findProjectIfExistsByProjectId(request.projectId());

        var msg = format("%s wants to join your %s project!", user.getUsername(), project.getProjectName());

        // Create notification data
        var data = new NotificationObject(project.getProjectId(), user.getUserId());

        // Convert data to json.
        var dataToJson = doForDataService(() -> m_objectMapper.writeValueAsString(data), "Converter Error!");

        // Project owner to user message
        var notificationMessage = new ProjectParticipantNotificationDTO.Builder()
                .setFromUserId(user.getUserId())
                .setToUserId(project.getProjectOwner().getUserId())
                .setMessage(msg)
                .setNotificationType(NotificationType.REQUEST)
                .setNotificationData(dataToJson)
                .setNotificationLink("none")
                .setNotificationImage(null)
                .setNotificationTitle("Project Join Request")
                .setNotificationDataType(NotificationDataType.PROJECT_JOIN_REQUEST)
                .setApproveLink(m_approvalLink)
                .setRejectLink(null)
                .setRequestId(request.requestId())
                .build();

        // Send notification to project owner
        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(notificationMessage),
                "ProjectService::sendNotificationToProjectOwner");
    }

    /**
     * Retrieves an overview of a specific project.
     *
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectOverviewDTO.
     */
    private ResponseMessage<Object> findProjectOverviewCallback(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var tags = findTagList(project);

        /*var img = m_s3Service.getImage(project.getProjectImagePath());
        project.setProjectImagePath(img);*/
        var projectOverviewDTO = m_projectMapper.toProjectOverviewDTO(project, tags);

        return new ResponseMessage<>("Project is found!", OK, projectOverviewDTO);
    }

    /**
     * Retrieves a paginated list of all projects for discovery purposes.
     *
     * @param page The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of project discovery views.
     */
    private MultipleResponseMessagePageable<Object> findAllProjectDiscoveryViewCallback(int page)
    {
        var projectPageable = m_serviceHelper.findAllValidProjects(page);

        var resultDTO = m_projectMapper.toProjectsDiscoveryDTO(toListConcurrent(projectPageable.getContent(), m_projectMapper::toProjectDiscoveryDTO));

        return new MultipleResponseMessagePageable<>(projectPageable.getTotalPages(), page, projectPageable.getNumberOfElements(),
                "Projects found!", resultDTO);
    }

    /**
     * Retrieves a view of a project from the perspective of its owner.
     *
     * @param userId    The UUID of the owner.
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectOwnerViewDTO.
     */
    private ResponseMessage<Object> findProjectOwnerViewCallback(UUID userId, UUID projectId)
    {
        var user = findUserIfExists(userId);
        var project = findProjectIfExistsByProjectId(projectId);

        if (!project.getProjectOwner().getUserId().equals(user.getUserId()))
            throw new DataServiceException("You are not owner of this project!");

        var result = m_projectMapper.toProjectOwnerViewDTO(project, findTagList(project), findProjectParticipantsDTOByProjectId(project));

        return new ResponseMessage<>("Project is found!", OK, result);
    }

    /**
     * Handles the saving of a new project based on provided project details.
     *
     * @param projectDTO Data Transfer Object containing project information.
     * @return A ResponseMessage object indicating the success or failure of the project creation.
     */
    private ResponseMessage<Object> saveProjectCallback(ProjectSaveDTO projectDTO)
    {
        var user = findUserIfExists(projectDTO.userId());

        if (user.getOwnerProjectCount() >= Policy.OWNER_MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are owner of %d projects already!", Policy.OWNER_MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

        if (user.getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

        var project = new Project.Builder()
                .setStartDate(projectDTO.startDate())
                .setFeedbackTimeRange(projectDTO.feedbackTimeRange())
                .setProjectAccessType(projectDTO.projectAccessType())
                .setProjectOwner(user)
                .setProjectLevel(projectDTO.projectLevel())
                .setProfessionLevel(projectDTO.professionLevel())
                .setSector(projectDTO.sector())
                .setDegree(projectDTO.degree())
                .setInterviewType(projectDTO.interviewType())
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

        var tagList = toStream(m_projectTagServiceHelper
                .getAllProjectTagByProjectId(savedProject.getProjectId()))
                .toList();

        user.setOwnerProjectCount(user.getOwnerProjectCount() + 1);
        user.setTotalProjectCount(user.getTotalProjectCount() + 1);
        m_serviceHelper.addUser(user);

        return new ResponseMessage<>("Project Created Successfully!", CREATED, m_projectMapper.toProjectOverviewDTO(savedProject, tagList));
    }

    /**
     * Extracts the file extension from a given MultipartFile.
     *
     * @param file The MultipartFile whose extension is to be extracted.
     * @return A string representing the file extension.
     */
    private String getFileExtension(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains("."))
        {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    /**
     * Handles the saving of a new project with an additional file attachment.
     *
     * @param projectDTO Data Transfer Object containing project information.
     * @param file       The MultipartFile to be associated with the project.
     * @return A ResponseMessage object indicating the success or failure of the project creation.
     */
    private ResponseMessage<Object> saveProjectCallbackV2(ProjectSaveDTO projectDTO, MultipartFile file)
    {
        var user = findUserIfExists(projectDTO.userId());

        if (user.getOwnerProjectCount() >= Policy.OWNER_MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are owner of %d projects already!", Policy.OWNER_MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

        if (user.getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

        var imageLink = m_s3Service.uploadToS3AndGetUrl(file, UUID.randomUUID() + getFileExtension(file));

        var project = new Project.Builder()
                .setStartDate(projectDTO.startDate())
                .setFeedbackTimeRange(projectDTO.feedbackTimeRange())
                .setProjectAccessType(projectDTO.projectAccessType())
                .setProjectOwner(user)
                .setProjectLevel(projectDTO.projectLevel())
                .setProfessionLevel(projectDTO.professionLevel())
                .setSector(projectDTO.sector())
                .setDegree(projectDTO.degree())
                .setInterviewType(projectDTO.interviewType())
                .setProjectAim(projectDTO.projectAim())
                .setDescription(projectDTO.projectDescription())
                .setExpectedCompletionDate(projectDTO.expectedCompletionDate())
                .setApplicationDeadline(projectDTO.applicationDeadline())
                .setProjectImagePath(imageLink)
                .setProjectName(projectDTO.projectName())
                .setProjectSummary(projectDTO.projectSummary())
                .setSpecialRequirements(projectDTO.specialRequirements())
                .setTechnicalRequirements(projectDTO.technicalRequirements())
                .setMaxParticipant(projectDTO.maxParticipantCount())
                .build();

        var savedProject = m_serviceHelper.saveProject(project);

        saveTagsIfNotExists(projectDTO.tags(), savedProject);

        var tagList = toStream(m_projectTagServiceHelper
                .getAllProjectTagByProjectId(savedProject.getProjectId()))
                .toList();

        user.setOwnerProjectCount(user.getOwnerProjectCount() + 1);
        user.setTotalProjectCount(user.getTotalProjectCount() + 1);
        m_serviceHelper.addUser(user);

        return new ResponseMessage<>("Project Created Successfully!", CREATED, m_projectMapper.toProjectOverviewDTO(savedProject, tagList));
    }

    /**
     * Retrieves all projects a specific user is participating in, with pagination.
     *
     * @param userId The UUID of the user.
     * @param page   The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of participant projects.
     */
    private MultipleResponseMessagePageable<Object> findAllParticipantProjectByUserIdCallback(UUID userId, int page)
    {
        findUserIfExists(userId);
        var projectPageable = m_serviceHelper.findAllParticipantProjectByUserId(userId, page);
        var projects = toStreamConcurrent(projectPageable).toList();
        var totalPage = projectPageable.getTotalPages();

        var projectOverviewsDTO = m_projectMapper.toProjectOverviewsDTO(toListConcurrent(projects,
                project -> m_projectMapper.toProjectOverviewDTO(project, findTagList(project))));

        return new MultipleResponseMessagePageable<>(totalPage, page, projectPageable.getNumberOfElements(), "Projects found!", projectOverviewsDTO);
    }

    /**
     * Retrieves all projects owned by a specific user, with pagination.
     *
     * @param userId The UUID of the user who owns the projects.
     * @param page   The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of owner projects.
     */
    private MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserIdCallback(UUID userId, int page)
    {
        findUserIfExists(userId);
        var projects = m_serviceHelper.findAllProjectByProjectOwnerUserId(userId, page);

        var projectsDetailDTO = m_projectMapper.toProjectsDetailDTO(toList(projects.getContent(),
                obj -> m_projectMapper.toProjectDetailDTO(obj, findTagList(obj), findProjectParticipantsDTOByProjectId(obj))));

        return new MultipleResponseMessagePageable<>(projects.getTotalPages(), page, projects.stream().toList().size(), "Projects found!", projectsDetailDTO);
    }

    /**
     * Retrieves all projects owned by a specific user, identified by username, with pagination.
     *
     * @param username The username of the project owner.
     * @param page     The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of owner projects.
     */
    private MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsernameCallback(String username, int page)
    {
        var user = findUserIfExists(username);

        var projects = m_serviceHelper.findAllProjectByProjectOwnerUserId(user.getUserId(), page);


        var dtoList = m_projectMapper.toProjectsDetailDTO(toList(projects.getContent(),
                obj -> m_projectMapper.toProjectDetailDTO(obj, findTagList(obj), findProjectParticipantsDTOByProjectId(obj))));

        return new MultipleResponseMessagePageable<>(projects.getTotalPages(), page, projects.stream().toList().size(), "Projects found!", dtoList);
    }


    // -----------------------------------HELPER METHODS---------------------------------------------

    /**
     * Retrieves a project by its ID if it exists.
     *
     * @param projectId The unique identifier of the project.
     * @return The found Project entity.
     * @throws DataServiceException if the project is not found.
     */
    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_serviceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    /**
     * Retrieves a user by their UUID if they exist.
     *
     * @param userId The unique identifier of the user.
     * @return The found User entity.
     * @throws DataServiceException if the user is not found.
     */
    private User findUserIfExists(UUID userId)
    {
        var user = m_serviceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", userId));

        return user.get();
    }

    /**
     * Retrieves a user by their username if they exist.
     *
     * @param username The username of the user.
     * @return The found User entity.
     * @throws DataServiceException if the user is not found.
     */
    private User findUserIfExists(String username)
    {
        var user = m_serviceHelper.findUserByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", username));

        return user.get();
    }

    /**
     * Checks if the user has permission to perform actions on the project.
     *
     * @param project The project to check permissions against.
     * @param userId  The UUID of the user.
     * @throws DataServiceException if the user does not have permission.
     */
    private void checkProjectPermission(Project project, UUID userId)
    {
        if (project.getAdminOperationStatus() == AdminOperationStatus.BLOCKED)
            throw new DataServiceException("Project is blocked!");

        if (project.getProjectStatus() == EProjectStatus.CANCELED)
            throw new DataServiceException("Project is canceled!");

        if (!project.getProjectOwner().getUserId().equals(userId))
            throw new DataServiceException("You are not owner of this project!");
    }

    /**
     * Updates a project with new details provided in ProjectUpdateDTO.
     *
     * @param projectDTO The DTO containing updated project information.
     * @return A ResponseMessage indicating the outcome of the update operation.
     */
    private ResponseMessage<Object> updateProjectCallback(ProjectUpdateDTO projectDTO)
    {
        var project = findProjectIfExistsByProjectId(projectDTO.projectId());

        checkProjectPermission(project, projectDTO.userId());

        project.setStartDate(projectDTO.startDate());
        project.setFeedbackTimeRange(projectDTO.feedbackTimeRange());
        project.setProjectAccessType(projectDTO.projectAccessType());
        project.setProjectLevel(projectDTO.projectLevel());
        project.setProfessionLevel(projectDTO.professionLevel());
        project.setSector(projectDTO.sector());
        project.setDegree(projectDTO.degree());
        project.setInterviewType(projectDTO.interviewType());
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

        var overviewDTO = m_projectMapper.toProjectDetailDTO(project, tagList, findProjectParticipantsDTOByProjectId(project));

        return new ResponseMessage<>("Project is updated!", OK, overviewDTO);
    }

    /**
     * Updates a project with new details and an image file.
     *
     * @param projectDTO The DTO containing updated project information.
     * @param file       The MultipartFile containing the project's new image.
     * @return A ResponseMessage indicating the outcome of the update operation.
     */
    private ResponseMessage<Object> updateProjectCallbackV2(ProjectUpdateDTO projectDTO, MultipartFile file)
    {
        var project = findProjectIfExistsByProjectId(projectDTO.projectId());
        var imageLink = project.getProjectImagePath();
        System.out.println(imageLink);
        if (file != null)
            imageLink = m_s3Service.uploadToS3AndGetUrl(file, UUID.randomUUID() + getFileExtension(file));

        checkProjectPermission(project, projectDTO.userId());

        project.setStartDate(projectDTO.startDate());
        project.setFeedbackTimeRange(projectDTO.feedbackTimeRange());
        project.setProjectAccessType(projectDTO.projectAccessType());
        project.setProjectLevel(projectDTO.projectLevel());
        project.setProfessionLevel(projectDTO.professionLevel());
        project.setSector(projectDTO.sector());
        project.setDegree(projectDTO.degree());
        project.setInterviewType(projectDTO.interviewType());
        project.setProjectAim(projectDTO.projectAim());
        project.setDescription(projectDTO.projectDescription());
        project.setExpectedCompletionDate(projectDTO.expectedCompletionDate());
        project.setApplicationDeadline(projectDTO.applicationDeadline());
        project.setProjectImagePath(imageLink);
        project.setProjectName(projectDTO.projectName());
        project.setProjectSummary(projectDTO.projectSummary());
        project.setSpecialRequirements(projectDTO.specialRequirements());
        project.setTechnicalRequirements(projectDTO.technicalRequirements());
        project.setMaxParticipant(projectDTO.maxParticipantCount());


        var savedProject = m_serviceHelper.saveProject(project);

        saveTagsIfNotExists(projectDTO.tags(), savedProject);

        var tagList = toStream(m_projectTagServiceHelper
                .getAllProjectTagByProjectId(savedProject.getProjectId()))
                .toList();

        var overviewDTO = m_projectMapper.toProjectDetailDTO(project, tagList, findProjectParticipantsDTOByProjectId(project));

        return new ResponseMessage<>("Project is updated!", OK, overviewDTO);
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
     * Retrieves all participants of a specific project.
     *
     * @param obj The project for which participants are to be retrieved.
     * @return A ProjectsParticipantDTO containing the list of participants.
     */
    private ProjectsParticipantDTO findProjectParticipantsDTOByProjectId(Project obj)
    {
        var participants = m_serviceHelper.findAllProjectParticipantByProjectId(obj.getProjectId());
        return m_projectParticipantMapper.toProjectsParticipantDTO(toList(participants, m_projectParticipantMapper::toProjectParticipantDTO));
    }

    /**
     * Retrieves all participants of a specific project.
     *
     * @param id The UUID of the project for which participants are to be retrieved.
     * @return A ProjectsParticipantDTO containing the list of participants.
     */
    private Set<ProjectParticipant> findProjectParticipantsByProjectId(UUID id)
    {
        var project = findProjectIfExistsByProjectId(id);
        return project.getProjectParticipants();
    }


    /**
     * Saves new tags for a project if they do not already exist.
     *
     * @param tags         The list of tags to be associated with the project.
     * @param savedProject The project to which tags are to be saved.
     */
    private void saveTagsIfNotExists(List<String> tags, Project savedProject)
    {
        var projectTags = toStreamConcurrent(m_projectTagServiceHelper
                .getAllProjectTagByProjectId(savedProject.getProjectId()));
        projectTags.map(ProjectTag::getId).forEach(m_projectTagRepository::deleteById);
        for (var tag : tags)
        {
            var tagName = convert(tag);
            if (m_tagServiceHelper.existsByTagNameContainsIgnoreCase(tagName)) // If tag already exists then save project tag
            {
                m_projectTagServiceHelper.saveProjectTag(new ProjectTag(tagName, savedProject.getProjectId()));
            } else // If tag not exists then save tag and project tag
            {
                //System.out.println("here-3");
                m_tagServiceHelper.saveTag(new Tag(tagName));
                m_projectTagServiceHelper.saveProjectTag(new ProjectTag(tagName, savedProject.getProjectId()));
            }

        }

    }


}
