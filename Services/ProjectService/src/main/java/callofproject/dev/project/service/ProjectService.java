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
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import callofproject.dev.project.util.Policy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

@Service(PROJECT_SERVICE)
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final TagServiceHelper m_tagServiceHelper;
    private final KafkaProducer m_kafkaProducer;
    private final ObjectMapper m_objectMapper;
    private final IProjectMapper m_projectMapper;
    private final S3Service m_s3Service;
    private final IProjectParticipantMapper m_projectParticipantMapper;

    @Value("${notification.request.approve}")
    private String m_approvalLink;

    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper,
                          @Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper projectTagServiceHelper,
                          @Qualifier(TAG_SERVICE_HELPER_BEAN_NAME) TagServiceHelper tagServiceHelper, KafkaProducer kafkaProducer, ObjectMapper objectMapper,
                          IProjectMapper projectMapper, S3Service s3Service, IProjectParticipantMapper projectParticipantMapper)
    {
        m_serviceHelper = serviceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_kafkaProducer = kafkaProducer;
        m_objectMapper = objectMapper;
        m_projectMapper = projectMapper;
        m_s3Service = s3Service;
        m_projectParticipantMapper = projectParticipantMapper;
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

    public ResponseMessage<Object> saveProjectV2(ProjectSaveDTO saveDTO, MultipartFile file)
    {
        return doForDataService(() -> saveProjectCallbackV2(saveDTO, file), "ProjectService::saveProject");
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
        return doForDataService(() -> findAllParticipantProjectByUserIdCallback(userId, page), "ProjectService::findAllParticipantProjectByUserId");
    }


    /**
     * Update project with given dto class.
     *
     * @param projectDTO represent the dto class
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> updateProject(ProjectUpdateDTO projectDTO)
    {
        return doForDataService(() -> updateProjectCallback(projectDTO), "ProjectService::updateProject");
    }


    /**
     * Find all project by user id who is owner in project.
     *
     * @param userId represent the user id
     * @param page   represent the page
     * @return ProjectOverviewsDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserId(UUID userId, int page)
    {
        return doForDataService(() -> findAllOwnerProjectsByUserIdCallback(userId, page), "ProjectService::findAllOwnerProjectsByUserId");
    }

    /**
     * Find all project by username who is owner in project.
     *
     * @param username represent the username
     * @param page     represent the page
     * @return ProjectOverviewsDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsername(String username, int page)
    {
        return doForDataService(() -> findAllOwnerProjectsByUsernameCallback(username, page), "ProjectService::findAllOwnerProjectsByUsername");
    }


    /**
     * Find project by project id.
     *
     * @param projectId represent the project id
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> findProjectOwnerView(UUID userId, UUID projectId)
    {
        return doForDataService(() -> findProjectOwnerViewCallback(userId, projectId), "ProjectService::findProjectOwnerView");
    }

    /**
     * Find all project.
     *
     * @param page represent the page
     * @return ProjectOverviewsDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllProjectDiscoveryView(int page)
    {
        return doForDataService(() -> findAllProjectDiscoveryViewCallback(page), "ProjectService::findAllProjectDiscoveryView");
    }

    /**
     * Find project by project id.
     *
     * @param projectId represent the project id
     * @return ResponseMessage.
     */
    public ResponseMessage<Object> findProjectOverview(UUID projectId)
    {
        return doForDataService(() -> findProjectOverviewCallback(projectId), "ProjectService::findProjectOverview");
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
        var result = doForDataService(() -> addProjectJoinRequestCallback(projectId, userId), "ProjectService::addProjectJoinRequest");

        var request = (ProjectParticipantRequestDTO) result.getObject();

        if (result.getStatusCode() == OK)
        {
            sendNotificationToProjectOwner(request);
        }
        return result;
    }

    public ResponseMessage<Object> findProjectDetail(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var tags = findTagList(project);

      /*  var img = m_s3Service.getImage(project.getProjectImagePath());
        project.setProjectImagePath(img);*/
        var projectDetailDTO = m_projectMapper.toProjectDetailDTO(project, tags, findProjectParticipantsByProjectId(project));

        return new ResponseMessage<>("Project is found!", OK, projectDetailDTO);
    }

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

        var projectDetailDTO = m_projectMapper.toProjectDetailDTO(project, tags, findProjectParticipantsByProjectId(project));

        return new ResponseMessage<>("Project is found!", OK, projectDetailDTO);
    }

    //-----------------------------------CALLBACKS---------------------------------------------

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

        var result = doForDataService(() -> m_serviceHelper.sendParticipantRequestToProject(new ProjectParticipantRequest(project.get(), user.get())),
                "ProjectService::addProjectJoinRequest");

        var dto = new ProjectParticipantRequestDTO(userId, projectId, result.getParticipantRequestId());
        return new ResponseMessage<>("Participant request is sent!", OK, dto);
    }

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

    private ResponseMessage<Object> findProjectOverviewCallback(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var tags = findTagList(project);

        /*var img = m_s3Service.getImage(project.getProjectImagePath());
        project.setProjectImagePath(img);*/
        var projectOverviewDTO = m_projectMapper.toProjectOverviewDTO(project, tags);

        return new ResponseMessage<>("Project is found!", OK, projectOverviewDTO);
    }

    private MultipleResponseMessagePageable<Object> findAllProjectDiscoveryViewCallback(int page)
    {
        var projectPageable = m_serviceHelper.findAllValidProjects(page);

        var resultDTO = m_projectMapper.toProjectsDiscoveryDTO(toListConcurrent(projectPageable.getContent(), m_projectMapper::toProjectDiscoveryDTO));

        return new MultipleResponseMessagePageable<>(projectPageable.getTotalPages(), page, projectPageable.getNumberOfElements(),
                "Projects found!", resultDTO);
    }

    private ResponseMessage<Object> findProjectOwnerViewCallback(UUID userId, UUID projectId)
    {
        var user = findUserIfExists(userId);
        var project = findProjectIfExistsByProjectId(projectId);

        if (!project.getProjectOwner().getUserId().equals(user.getUserId()))
            throw new DataServiceException("You are not owner of this project!");

        var result = m_projectMapper.toProjectOwnerViewDTO(project, findTagList(project), findProjectParticipantsByProjectId(project));

        return new ResponseMessage<>("Project is found!", OK, result);
    }

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

    public String getFileExtension(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains("."))
        {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

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
        System.out.println("here");

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

    private MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserIdCallback(UUID userId, int page)
    {
        findUserIfExists(userId);
        var projects = m_serviceHelper.findAllProjectByProjectOwnerUserId(userId, page);

        var projectsDetailDTO = m_projectMapper.toProjectsDetailDTO(toList(projects.getContent(),
                obj -> m_projectMapper.toProjectDetailDTO(obj, findTagList(obj), findProjectParticipantsByProjectId(obj))));

        return new MultipleResponseMessagePageable<>(projects.getTotalPages(), page, projects.stream().toList().size(), "Projects found!", projectsDetailDTO);
    }

    private MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsernameCallback(String username, int page)
    {
        var user = findUserIfExists(username);

        var projects = m_serviceHelper.findAllProjectByProjectOwnerUserId(user.getUserId(), page);


        var dtoList = m_projectMapper.toProjectsDetailDTO(toList(projects.getContent(),
                obj -> m_projectMapper.toProjectDetailDTO(obj, findTagList(obj), findProjectParticipantsByProjectId(obj))));

        return new MultipleResponseMessagePageable<>(projects.getTotalPages(), page, projects.stream().toList().size(), "Projects found!", dtoList);
    }


    // -----------------------------------HELPER METHODS---------------------------------------------

    private Project findProjectIfExistsByProjectId(UUID projectId)
    {
        var project = m_serviceHelper.findProjectById(projectId);

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    private User findUserIfExists(UUID userId)
    {
        var user = m_serviceHelper.findUserById(userId);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", userId));

        return user.get();
    }

    private User findUserIfExists(String username)
    {
        var user = m_serviceHelper.findUserByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", username));

        return user.get();
    }

    private void checkProjectPermission(Project project, UUID userId)
    {
        if (project.getAdminOperationStatus() == AdminOperationStatus.BLOCKED)
            throw new DataServiceException("Project is blocked!");

        if (project.getProjectStatus() == EProjectStatus.CANCELED)
            throw new DataServiceException("Project is canceled!");

        if (!project.getProjectOwner().getUserId().equals(userId))
            throw new DataServiceException("You are not owner of this project!");
    }

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

        var overviewDTO = m_projectMapper.toProjectDetailDTO(project, tagList, findProjectParticipantsByProjectId(project));

        return new ResponseMessage<>("Project is updated!", OK, overviewDTO);
    }

    private List<ProjectTag> findTagList(Project obj)
    {
        return toStream(m_projectTagServiceHelper.getAllProjectTagByProjectId(obj.getProjectId())).toList();
    }

    private ProjectsParticipantDTO findProjectParticipantsByProjectId(Project obj)
    {
        var participants = m_serviceHelper.findAllProjectParticipantByProjectId(obj.getProjectId());
        return m_projectParticipantMapper.toProjectsParticipantDTO(toList(participants, m_projectParticipantMapper::toProjectParticipantDTO));
    }

    private void saveTagsIfNotExists(List<String> tags, Project savedProject)
    {
        for (var tag : tags)
        {
            var tagName = convert(tag);
            if (m_tagServiceHelper.existsByTagNameContainsIgnoreCase(tagName)) // If tag already exists then save project tag
                m_projectTagServiceHelper.saveProjectTag(new ProjectTag(tagName, savedProject.getProjectId()));

            else // If tag not exists then save tag and project tag
            {
                m_tagServiceHelper.saveTag(new Tag(tagName));
                m_projectTagServiceHelper.saveProjectTag(new ProjectTag(tagName, savedProject.getProjectId()));
            }
        }

        // If project tags not contains string tags, then remove project tag.
        //var projectTags = toStreamConcurrent(m_projectTagServiceHelper.getAllProjectTagByProjectId(savedProject.getProjectId())).toList();

        //projectTags.stream().filter(projectTag -> !tags.contains(projectTag.getTagName())).forEach(m_projectTagServiceHelper::removeProjectTag);
    }


}
