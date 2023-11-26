package callofproject.dev.project.service;


import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipants;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.project.dto.ProjectSaveDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.util.UUID.fromString;


@Service
@Lazy
public class ProjectService
{
    private final ProjectServiceHelper m_serviceHelper;

    public ProjectService(@Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper)
    {
        m_serviceHelper = serviceHelper;
    }

    public Project saveProject(ProjectSaveDTO projectDTO)
    {
        return doForDataService(() -> saveProjectCallback(projectDTO), "ProjectService::saveProject");
    }


    public Project saveProjectCallback(ProjectSaveDTO projectDTO)
    {
        var projectAccessType = m_serviceHelper.findProjectAccessTypeByProjectAccessType(projectDTO.projectAccessType());
        var projectLevelType = m_serviceHelper.findProjectLevelByProjectLevel(projectDTO.projectLevel());
        var professionLevelType = m_serviceHelper.findProjectProfessionLevelByProjectProfessionLevel(projectDTO.professionLevel());
        var sectorType = m_serviceHelper.findSectorBySector(projectDTO.sector());
        var degreeType = m_serviceHelper.findDegreeByDegree(projectDTO.degree());
        var interviewTypeType = m_serviceHelper.findInterviewTypeByInterviewType(projectDTO.interviewType());

        if (projectAccessType.isEmpty() || projectLevelType.isEmpty() || professionLevelType.isEmpty() || sectorType.isEmpty() || degreeType.isEmpty() || interviewTypeType.isEmpty())
            throw new DataServiceException("Project Access Type or Project Level or Profession Level or Sector or Degree or Interview Type is not found!");

        var project = new Project.Builder()
                .setExpectedProjectDeadline(projectDTO.expectedProjectDeadline())
                .setProjectAccessType(projectAccessType.get())
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
                .setProjectOwnerId(projectDTO.userId())
                .build();

        var savedProject = m_serviceHelper.saveProject(project);
        m_serviceHelper.saveProjectToUser(projectDTO.userId(), savedProject.getProjectId());

        return savedProject;
    }

    public Iterable<Project> findProjectsByUserId(String userId)
    {
        return doForDataService(() -> findProjectsByUserId(userId), "ProjectService::findProjectsByUserId");
    }

    public Iterable<Project> findAll()
    {
        return m_serviceHelper.findAllProjectsPageable(1);
    }


    public ProjectParticipants addParticipant(String projectId, String userId)
    {
        return doForDataService(() -> m_serviceHelper.saveParticipantToProject(fromString(userId), fromString(projectId)),
                "ProjectService::addParticipant");
    }
}
