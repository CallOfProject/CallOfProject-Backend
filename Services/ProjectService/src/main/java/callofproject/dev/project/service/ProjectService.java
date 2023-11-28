package callofproject.dev.project.service;


import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.project.dto.ProjectOverviewDTO;
import callofproject.dev.project.dto.ProjectOverviewsDTO;
import callofproject.dev.project.dto.ProjectSaveDTO;
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
import static java.lang.String.format;
import static java.util.UUID.fromString;


@Service
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;
    private final ProjectTagServiceHelper m_tagServiceHelper;
    private final IProjectMapper m_projectMapper;

    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper,
                          @Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper tagServiceHelper, IProjectMapper projectMapper)
    {
        m_serviceHelper = serviceHelper;
        m_tagServiceHelper = tagServiceHelper;
        m_projectMapper = projectMapper;
    }

    public ProjectOverviewDTO saveProject(ProjectSaveDTO projectDTO)
    {
        return doForDataService(() -> saveProjectCallback(projectDTO), "ProjectService::saveProject");
    }


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

    private void validateProjectRequirements(ProjectSaveDTO projectDTO)
    {

    }

    public Iterable<Project> findProjectsByUserId(String userId)
    {
        return doForDataService(() -> findProjectsByUserId(userId), "ProjectService::findProjectsByUserId");
    }

    public Iterable<Project> findAll()
    {
        return m_serviceHelper.findAllProjectsPageable(1);
    }


    public boolean addParticipant(String projectId, String userId)
    {
        return doForDataService(() -> m_serviceHelper.addProjectParticipant(fromString(projectId), fromString(userId)),
                "ProjectService::addParticipant");
    }

    public ProjectOverviewsDTO findAllParticipantProjectByUserId(UUID userId, int page)
    {
        var projects = m_serviceHelper.findAllParticipantProjectByUserId(userId, page);
        var projectOverviewList = new ArrayList<ProjectOverviewDTO>();
        for (var project : projects)
        {
            var tags = StreamUtil.toStream(m_tagServiceHelper.getAllProjectTagByProjectId(project.getProjectId())).toList();
            projectOverviewList.add(m_projectMapper.toProjectOverviewDTO(project, tags));
        }

        return doForDataService(() -> m_projectMapper.toProjectOverviewsDTO(projectOverviewList), "ProjectService::findAllParticipantProjectByUserId");
    }
}
