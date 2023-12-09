package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.dal.TagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import callofproject.dev.nosql.entity.Tag;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import callofproject.dev.project.dto.ProjectsParticipantDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import callofproject.dev.project.util.Policy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.common.status.Status.*;
import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.util.stream.StreamUtil.*;
import static java.lang.String.format;

@Service
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;
    private final ProjectTagServiceHelper m_projectTagServiceHelper;
    private final TagServiceHelper m_tagServiceHelper;
    private final IProjectMapper m_projectMapper;
    private final IProjectParticipantMapper m_projectParticipantMapper;

    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper,
                          @Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper projectTagServiceHelper,
                          @Qualifier(TAG_SERVICE_HELPER_BEAN_NAME) TagServiceHelper tagServiceHelper,
                          IProjectMapper projectMapper, IProjectParticipantMapper projectParticipantMapper)
    {
        m_serviceHelper = serviceHelper;
        m_projectTagServiceHelper = projectTagServiceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectMapper = projectMapper;
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


    //-----------------------------------CALLBACKS---------------------------------------------

    private ResponseMessage<Object> findProjectOverviewCallback(UUID projectId)
    {
        var project = findProjectIfExistsByProjectId(projectId);
        var tags = findTagList(project);

        var projectOverviewDTO = m_projectMapper.toProjectOverviewDTO(project, tags);

        return new ResponseMessage<>("Project is found!", OK, projectOverviewDTO);
    }

    private MultipleResponseMessagePageable<Object> findAllProjectDiscoveryViewCallback(int page)
    {
        var projectPageable = m_serviceHelper.findAllValidProjects(page);
        var resultDTO = m_projectMapper.toProjectsDiscoveryDTO(toListConcurrent(projectPageable.getContent(),
                m_projectMapper::toProjectDiscoveryDTO));

        return new MultipleResponseMessagePageable<>(projectPageable.getTotalPages(), page, projectPageable.getNumberOfElements(), "Projects found!", resultDTO);
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
        var user = m_serviceHelper.findUserById(projectDTO.userId());

        if (user.isEmpty())
            throw new DataServiceException(format("User with id: %s is not found!", projectDTO.userId()));

        if (user.get().getOwnerProjectCount() >= Policy.OWNER_MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You are owner of %d projects already!", Policy.OWNER_MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

        if (user.get().getTotalProjectCount() >= Policy.MAX_PROJECT_COUNT)
            return new ResponseMessage<>(format("You cannot create or join to project! Max project count is: %d", Policy.MAX_PROJECT_COUNT),
                    NOT_ACCEPTED, false);

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

        var tagList = toStream(m_projectTagServiceHelper
                .getAllProjectTagByProjectId(savedProject.getProjectId()))
                .toList();

        user.get().setOwnerProjectCount(user.get().getOwnerProjectCount() + 1);
        user.get().setTotalProjectCount(user.get().getTotalProjectCount() + 1);
        m_serviceHelper.addUser(user.get());

        return new ResponseMessage<>("Project Created Successfully!", CREATED, m_projectMapper.toProjectOverviewDTO(savedProject, tagList));
    }


    private MultipleResponseMessagePageable<Object> findAllParticipantProjectByUserIdCallback(UUID userId, int page)
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

    private MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserIdCallback(UUID userId, int page)
    {
        var projects = m_serviceHelper.findAllProjectByProjectOwnerUserId(userId, page);

        if (projects.isEmpty())
            throw new DataServiceException(format("User with id: %s is not owner in any project!", userId));

        var dtoList = m_projectMapper.toProjectsDetailDTO(toList(projects.getContent(),
                obj -> m_projectMapper.toProjectDetailDTO(obj, findTagList(obj), findProjectParticipantsByProjectId(obj))));

        return new MultipleResponseMessagePageable<>(projects.getTotalPages(), page, projects.stream().toList().size(), "Projects found!", dtoList);
    }

    private MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsernameCallback(String username, int page)
    {
        var user = findUserIfExists(username);

        var projects = m_serviceHelper.findAllProjectByProjectOwnerUserId(user.getUserId(), page);

        if (projects.isEmpty())
            return new MultipleResponseMessagePageable<>(projects.getTotalPages(), page, projects.stream().toList().size(), "Projects not found!", null);

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


    private Project findProjectById(UUID projectId)
    {
        var project = doForDataService(() -> m_serviceHelper.findProjectById(projectId), "ProjectService::findProjectById");

        if (project.isEmpty())
            throw new DataServiceException(format("Project with id: %s is not found!", projectId));

        return project.get();
    }

    private ResponseMessage<Object> updateProjectCallback(ProjectUpdateDTO projectDTO)
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
            if (m_tagServiceHelper.existsByTagNameContainsIgnoreCase(tag.replaceAll(" ", ""))) // If tag already exists then save project tag
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


}
