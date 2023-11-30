package callofproject.dev.project.service;


import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.dto.*;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.util.stream.StreamUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.util.stream.StreamUtil.toStreamConcurrent;
import static java.lang.String.format;
import static java.util.UUID.fromString;


@Service
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;
    private final ProjectTagServiceHelper m_tagServiceHelper;
    private final IProjectMapper m_projectMapper;
    private final KafkaProducer m_kafkaProducer;

    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper,
                          @Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper tagServiceHelper, IProjectMapper projectMapper, KafkaProducer kafkaProducer)
    {
        m_serviceHelper = serviceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectMapper = projectMapper;
        m_kafkaProducer = kafkaProducer;
    }

    public ProjectOverviewDTO saveProject(ProjectSaveDTO projectDTO)
    {
        return doForDataService(() -> saveProjectCallback(projectDTO), "ProjectService::saveProject");
    }

    /**
     * Save Project with given dto class.
     *
     * @param projectDTO represent the dto class
     * @return ProjectOverviewDTO.
     */
    public ProjectOverviewDTO saveProjectCallback(ProjectSaveDTO projectDTO)
    {
        var projectAccessType = m_serviceHelper.findProjectAccessTypeByProjectAccessType(projectDTO.projectAccessType());
        var projectLevelType = m_serviceHelper.findProjectLevelByProjectLevel(projectDTO.projectLevel());
        var professionLevelType = m_serviceHelper.findProjectProfessionLevelByProjectProfessionLevel(projectDTO.professionLevel());
        var sectorType = m_serviceHelper.findSectorBySector(projectDTO.sector());
        var degreeType = m_serviceHelper.findDegreeByDegree(projectDTO.degree());
        var interviewTypeType = m_serviceHelper.findInterviewTypeByInterviewType(projectDTO.interviewType());

        if (projectAccessType.isEmpty() || projectLevelType.isEmpty() || professionLevelType.isEmpty() || sectorType.isEmpty() || degreeType.isEmpty() || interviewTypeType.isEmpty())
            throw new DataServiceException("Project Access Type or Project Level or Profession Level or Sector or Degree or Interview Type is not found!");
        var user = m_serviceHelper.findUserById(projectDTO.userId());

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", projectDTO.userId()));

        var project = new Project.Builder()
                .setExpectedProjectDeadline(projectDTO.expectedProjectDeadline())
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
        var tagList = StreamUtil.toStream(m_tagServiceHelper.getAllProjectTagByProjectId(savedProject.getProjectId())).toList();
        return m_projectMapper.toProjectOverviewDTO(savedProject, tagList);
    }


    public Iterable<Project> findProjectsByUserId(String userId)
    {
        return doForDataService(() -> findProjectsByUserId(userId), "ProjectService::findProjectsByUserId");
    }

    public Iterable<Project> findAll()
    {
        return m_serviceHelper.findAllProjectsPageable(1);
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
    public boolean addProjectJoinRequest(String projectId, String userId)
    {
        var result = doForDataService(() -> m_serviceHelper.sendParticipantRequestToProject(fromString(projectId), fromString(userId)),
                "ProjectService::addProjectJoinRequest");

        if (result)
            sendNotificationToProjectOwner(userId, projectId);

        return result;
    }

    private void sendNotificationToProjectOwner(String userId, String projectId)
    {
        var user = m_serviceHelper.findUserById(fromString(userId));
        var project = m_serviceHelper.findProjectById(fromString(projectId));

        if (user.isEmpty() || project.isEmpty())
            throw new DataServiceException(format("User with id: %s or Project with id: %s is not found!", userId, projectId));

        var msg = format("%s wants to join your %s project!", user.get().getUsername(), project.get().getProjectName());
        var participant = new ProjectParticipantRequestDTO(user.get().getUsername(), msg);

        doForDataService(() -> m_kafkaProducer.sendProjectParticipantNotification(participant),
                "ProjectService::sendNotificationToProjectOwner");
    }

    /**
     * Find all project by user id who is participant in project.
     *
     * @param userId represent the user id
     * @param page   represent the page
     * @return ProjectOverviewsDTO class.
     */
    public ProjectOverviewsDTO findAllParticipantProjectByUserId(UUID userId, int page)
    {
        var projects = toStreamConcurrent(m_serviceHelper.findAllParticipantProjectByUserId(userId, page)).toList();

        if (projects.isEmpty())
            throw new DataServiceException(format("User with id: %s is not participant in any project!", userId));

        var projectOverviewList = new ArrayList<ProjectOverviewDTO>();

        for (var project : projects)
        {
            var tags = toStreamConcurrent(m_tagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();
            projectOverviewList.add(m_projectMapper.toProjectOverviewDTO(project, tags));
        }

        return doForDataService(() -> m_projectMapper.toProjectOverviewsDTO(projectOverviewList), "ProjectService::findAllParticipantProjectByUserId");
    }


    /**
     * Find all project by user id who is owner in project.
     *
     * @param requestDTO represent the requestDTO class
     * @return ProjectOverviewsDTO class.
     */
    public boolean approveParticipantRequest(ParticipantRequestDTO requestDTO)
    {
        var participantRequest = m_serviceHelper.findProjectParticipantRequestByParticipantRequestId(requestDTO.requestId());

        if (participantRequest.isEmpty())
            throw new DataServiceException(format("Participant request with id: %s is not found!", requestDTO.requestId()));

        if (participantRequest.get().isAccepted())
            throw new DataServiceException(format("Participant request with id: %s is already accepted!", requestDTO.requestId()));

        participantRequest.get().setAccepted(requestDTO.isAccepted());

        var updatedRequest = doForDataService(() -> m_serviceHelper.saveProjectParticipantRequest(participantRequest.get()),
                "ProjectService::approveParticipantRequest");
        if (updatedRequest == null)
            throw new DataServiceException(format("Participant request with id: %s is not updated!", requestDTO.requestId()));

        return addParticipant(new SaveProjectParticipantDTO(participantRequest.get().getUser().getUserId(),
                participantRequest.get().getProject().getProjectId()));
    }
}
