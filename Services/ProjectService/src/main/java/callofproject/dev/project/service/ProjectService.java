package callofproject.dev.project.service;


import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.project.dto.ProjectSaveDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;


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
        try
        {
            System.out.println("here0");
            var accessType = projectDTO.projectAccessType();
            var projectLevel = projectDTO.projectLevel();
            var professionLevel = projectDTO.professionLevel();
            var sector = projectDTO.sector();
            var degree = projectDTO.degree();
            var interviewType = projectDTO.interviewType();


            var projectAccessType = m_serviceHelper.findProjectAccessTypeByProjectAccessType(accessType);
            var projectLevelType = m_serviceHelper.findProjectLevelByProjectLevel(projectLevel);
            var professionLevelType = m_serviceHelper.findProjectProfessionLevelByProjectProfessionLevel(professionLevel);
            var sectorType = m_serviceHelper.findSectorBySector(sector);
            var degreeType = m_serviceHelper.findDegreeByDegree(degree);
            var interviewTypeType = m_serviceHelper.findInterviewTypeByInterviewType(interviewType);

            if (projectAccessType.isEmpty() || projectLevelType.isEmpty() || professionLevelType.isEmpty() || sectorType.isEmpty() || degreeType.isEmpty() || interviewTypeType.isEmpty())
                throw new Exception("Project Access Type or Project Level or Profession Level or Sector or Degree or Interview Type is not found!");

            System.out.println("here1");
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
                    .setMaxParticipant(23)
                    .build();
            System.out.println("here2");
            return m_serviceHelper.saveProject(project);
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }


    public Iterable<Project> findAll()
    {
        return m_serviceHelper.findAllProjectsPageable(1);
    }


}
