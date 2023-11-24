/**
 * ProjectServiceHelper.java created at 11.12.2023 22:01:59
 * by CallOfProjectTeam
 */

package callofproject.dev.data.project.dal;

import callofproject.dev.data.project.entity.*;
import callofproject.dev.data.project.entity.enums.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.data.project.ProjectRepositoryBeanName.REPOSITORY_FACADE_BEAN;
import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.util.stream.StreamUtil.toList;
import static java.util.UUID.fromString;

@Component(PROJECT_SERVICE_HELPER_BEAN)
@PropertySource("classpath:application-project_repository.properties")
@SuppressWarnings("all")
@Lazy
public class ProjectServiceHelper
{
    @Value("${project.page.default-size}")
    private int m_defaultPageSize;
    private final RepositoryFacade m_facade;

    public ProjectServiceHelper(@Qualifier(REPOSITORY_FACADE_BEAN) RepositoryFacade facade)
    {
        m_facade = facade;
    }


    /**
     * Remove project by id.
     *
     * @param projectId represent the project id.
     */
    public void removeProjectById(UUID projectId)
    {
        doForRepository(() -> m_facade.m_projectRepository.deleteById(projectId), "ProjectServiceHelper::removeProjectById");
    }

    /**
     * Save project.
     *
     * @param project represent the project.
     * @return Project
     */
    public Project saveProject(Project project)
    {
        return doForRepository(() -> m_facade.m_projectRepository.save(project),
                "ProjectServiceHelper::saveProject");
    }

    /**
     * Find all projects.
     *
     * @param page represent the page.
     * @return Project
     */
    public Iterable<Project> findAllProjectsPageable(int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAll(pageable),
                "ProjectServiceHelper::findAllProjects");
    }


    /**
     * Find all projects by project name.
     *
     * @param projectName represent the project name.
     * @param page        represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByProjectName(String projectName, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectName(projectName, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectName");
    }

    /**
     * Find all projects by project name containing.
     *
     * @param word represent the word.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByProjectNameContaining(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectNameContaining(word, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectNameContaining");
    }

    /**
     * Find all projects by application deadline.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByApplicationDeadline(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadline(date, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadline");
    }

    /**
     * Find all projects by project summary containing ignore case.
     *
     * @param word represent the word.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByDescriptionContainingIgnoreCase(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByDescriptionContainingIgnoreCase(word, pageable),
                "ProjectServiceHelper::findAllProjectsByDescriptionContainingIgnoreCase");
    }


    /**
     * Find all projects by application deadline after.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByApplicationDeadlineAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadlineAfter(date, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadlineAfter");
    }

    /**
     * Find all projects by application deadline before.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByApplicationDeadlineBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadlineBefore(date, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadlineBefore");
    }

    /**
     * Find all projects by application deadline between.
     *
     * @param start represent the start date.
     * @param end   represent the end date.
     * @param page  represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByApplicationDeadlineBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadlineBetween(start, end, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadlineBetween");
    }

    /**
     * Find all projects by expected completion date.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedCompletionDate(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDate(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDate");
    }

    /**
     * Find all projects by expected completion date after.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedCompletionDateAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDateAfter(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDateAfter");
    }

    /**
     * Find all projects by expected completion date before.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedCompletionDateBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDateBefore(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDateBefore");
    }

    /**
     * Find all projects by expected completion date between.
     *
     * @param start represent the start date.
     * @param end   represent the end date.
     * @param page  represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedCompletionDateBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDateBetween(start, end, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDateBetween");
    }

    /**
     * Find all projects by expected project deadline.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedProjectDeadline(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadline(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadline");
    }

    /**
     * Find all projects by expected project deadline after.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedProjectDeadlineAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadlineAfter(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadlineAfter");
    }

    /**
     * Find all projects by expected project deadline before.
     *
     * @param date represent the date.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedProjectDeadlineBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadlineBefore(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadlineBefore");
    }

    /**
     * Find all projects by expected project deadline between.
     *
     * @param start represent the start date.
     * @param end   represent the end date.
     * @param page  represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByExpectedProjectDeadlineBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadlineBetween(start, end, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadlineBetween");
    }

    /**
     * Find all projects by max participant.
     *
     * @param maxParticipant represent the max participant.
     * @param page           represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByMaxParticipant(int maxParticipant, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByMaxParticipant(maxParticipant, pageable),
                "ProjectServiceHelper::findAllProjectsByMaxParticipant");
    }

    /**
     * Find all projects by max participant less than equal.
     *
     * @param maxParticipant represent the max participant.
     * @param page           represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByMaxParticipantLessThanEqual(int maxParticipant, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByMaxParticipantLessThanEqual(maxParticipant, pageable),
                "ProjectServiceHelper::findAllProjectsByMaxParticipantLessThanEqual");
    }

    /**
     * Find all projects by max participant greater than equal.
     *
     * @param minParticipant represent the min participant.
     * @param page           represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByMaxParticipantGreaterThanEqual(int minParticipant, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByMaxParticipantGreaterThanEqual(minParticipant, pageable),
                "ProjectServiceHelper::findAllProjectsByMaxParticipantGreaterThanEqual");
    }

    /**
     * Find all projects by invite link.
     *
     * @param link represent the link.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByInviteLink(String link, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByInviteLink(link, pageable),
                "ProjectServiceHelper::findAllProjectsByInviteLink");
    }

    /**
     * Find all projects by project access type.
     *
     * @param accessType represent the access type.
     * @param page       represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByProjectAccessType(EProjectAccessType accessType, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectAccessType(accessType, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectAccessType");
    }

    /**
     * Find all projects by project profession level.
     *
     * @param professionLevel represent the profession level.
     * @param page            represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByProjectProfessionLevel(EProjectProfessionLevel professionLevel, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProfessionLevel(professionLevel, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectProfessionLevel");
    }

    /**
     * Find all projects by sector.
     *
     * @param sector represent the sector.
     * @param page   represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsBySector(ESector sector, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllBySector(sector, pageable),
                "ProjectServiceHelper::findAllProjectsBySector");
    }

    /**
     * Find all projects by degree.
     *
     * @param degree represent the degree.
     * @param page   represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByDegree(EDegree degree, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByDegree(degree, pageable),
                "ProjectServiceHelper::findAllProjectsByDegree");
    }

    /**
     * Find all projects by project level.
     *
     * @param projectLevel represent the project level.
     * @param page         represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByProjectLevel(EProjectLevel projectLevel, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectLevel(projectLevel, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectLevel");
    }

    /**
     * Find all projects by interview type.
     *
     * @param interviewType represent the interview type.
     * @param page          represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByInterviewType(EInterviewType interviewType, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByInterviewType(interviewType, pageable),
                "ProjectServiceHelper::findAllProjectsByInterviewType");
    }

    /**
     * Find all projects by multiple criteria.
     *
     * @param projectName    represent the project name.
     * @param description    represent the description.
     * @param projectSummary represent the project summary.
     * @param projectAim     represent the project aim.
     * @param page           represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByMultipleCriteria(String projectName, String description, String projectSummary, String projectAim, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository
                .findAllByProjectNameAndDescriptionAndProjectSummaryAndProjectAimContainsIgnoreCase(projectName, description,
                        projectSummary, projectAim, pageable), "ProjectServiceHelper::findAllProjectsByMultipleCriteria");
    }

    /**
     * Find all projects by any criteria.
     *
     * @param projectName    represent the project name.
     * @param description    represent the description.
     * @param projectSummary represent the project summary.
     * @param projectAim     represent the project aim.
     * @param page           represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllProjectsByAnyCriteria(String projectName, String description, String projectSummary, String projectAim, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository
                .findAllByProjectNameOrDescriptionOrProjectSummaryOrProjectAimContainsIgnoreCase(projectName, description,
                        projectSummary, projectAim, pageable), "ProjectServiceHelper::findAllProjectsByAnyCriteria");
    }


    /**
     * Find all projects by project summary containing ignore case.
     *
     * @param word represent the word.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllByProjectNameContainingIgnoreCase(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectNameContainingIgnoreCase(word, pageable),
                "ProjectServiceHelper::findAllByProjectNameContainingIgnoreCase");
    }

    /**
     * Find all projects by project summary containing ignore case.
     *
     * @param word represent the word.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllByProjectSummaryContainingIgnoreCase(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectSummaryContainingIgnoreCase(word, pageable),
                "ProjectServiceHelper::findAllByProjectSummaryContainingIgnoreCase");
    }

    /**
     * Find all projects by project aim contains ignore case.
     *
     * @param word represent the word.
     * @param page represent the page.
     * @return Iterable<Project>
     */
    public Iterable<Project> findAllByProjectAimContainsIgnoreCase(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, m_defaultPageSize);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectAimContainsIgnoreCase(word, pageable),
                "ProjectServiceHelper::findAllByProjectAimContainsIgnoreCase");
    }


    /**
     * Find project access type by project access type enum.
     *
     * @param projectAccessType represent the project access type.
     * @return Optional<ProjectAccessType>
     */
    public Optional<ProjectAccessType> findProjectAccessTypeByProjectAccessType(EProjectAccessType projectAccessType)
    {
        return doForRepository(() -> m_facade.m_projectAccessTypeRepository.findProjectAccessTypeByProjectAccessType(projectAccessType),
                "ProjectServiceHelper::findProjectAccessTypeByProjectAccessType");
    }

    /**
     * Find project profession level by project profession level enum.
     *
     * @param projectProfessionLevel represent the project profession level.
     * @return Optional<ProjectProfessionLevel>
     */
    public Optional<ProjectProfessionLevel> findProjectProfessionLevelByProjectProfessionLevel(EProjectProfessionLevel projectProfessionLevel)
    {
        return doForRepository(() -> m_facade.m_professionLevelRepository.findProjectProfessionLevelByProjectProfessionLevel(projectProfessionLevel),
                "ProjectServiceHelper::findProjectProfessionLevelByProjectProfessionLevel");
    }

    /**
     * Find degree by degree enum.
     *
     * @param degree represent the degree.
     * @return Optional<Degree>
     */
    public Optional<Degree> findDegreeByDegree(EDegree degree)
    {
        return doForRepository(() -> m_facade.m_degreeRepository.findDegreeByDegree(degree),
                "ProjectServiceHelper::findDegreeByDegree");
    }

    /**
     * Find interview type by interview type enum.
     *
     * @param interviewType represent the interview type.
     * @return Optional<InterviewType>
     */
    public Optional<InterviewType> findInterviewTypeByInterviewType(EInterviewType interviewType)
    {
        return doForRepository(() -> m_facade.m_interviewTypeRepository.findInterviewTypeByInterviewType(interviewType),
                "ProjectServiceHelper::findInterviewTypeByInterviewType");
    }

    /**
     * Find project level by project level enum.
     *
     * @param projectLevel represent the project level.
     * @return Optional<ProjectLevel>
     */
    public Optional<ProjectLevel> findProjectLevelByProjectLevel(EProjectLevel projectLevel)
    {
        return doForRepository(() -> m_facade.m_projectLevelRepository.findProjectLevelByProjectLevel(projectLevel),
                "ProjectServiceHelper::findProjectLevelByProjectLevel");
    }

    /**
     * Find sector by sector enum.
     *
     * @param sector represent the sector.
     * @return Optional<Sector>
     */
    public Optional<Sector> findSectorBySector(ESector sector)
    {
        return doForRepository(() -> m_facade.m_sectorRepository.findSectorBySector(sector),
                "ProjectServiceHelper::findSectorBySector");
    }

    /**
     * Find All projects by user id.
     *
     * @param userId represent the user id.
     * @return Iterable<UserProjects>
     */
    public Iterable<UserProjects> findAllByUserId(UUID userId)
    {
        return doForRepository(() -> m_facade.m_userProjectsRepository.findAllByUserId(userId),
                "ProjectServiceHelper::findAllByUserId");
    }

    /**
     * Find All projects by project id.
     *
     * @param projectId represent the project id.
     * @return Iterable<UserProjects>
     */
    public Iterable<UserProjects> findAllByProjectId(UUID projectId)
    {
        return doForRepository(() -> m_facade.m_userProjectsRepository.findAllByProjectId(projectId),
                "ProjectServiceHelper::findAllByProjectId");
    }

    /**
     * Find by user id and project id.
     *
     * @param userId    represent the user id.
     * @param projectId represent the project id.
     * @return Optional<UserProjects>
     */
    public Optional<UserProjects> findByUserIdAndProjectId(UUID userId, UUID projectId)
    {
        return doForRepository(() -> m_facade.m_userProjectsRepository.findByUserIdAndProjectId(userId, projectId),
                "ProjectServiceHelper::findByUserIdAndProjectId");
    }

    /**
     * Save project to user.
     *
     * @param userId    represent the user id.
     * @param projectId represent the project id.
     * @return UserProjects
     */
    public UserProjects saveProjectToUser(UUID userId, UUID projectId)
    {
        return doForRepository(() -> m_facade.m_userProjectsRepository.save(new UserProjects(userId, projectId)),
                "ProjectServiceHelper::saveProjectToUser");
    }


    /**
     * Find all projects by user id.
     *
     * @param userId    represent the user id.
     * @param projectId represent the project id.
     */
    public Iterable<Project> findProjectsByUserId(String userId)
    {
        return doForRepository(() -> m_facade.m_projectRepository.findAllById(toList(m_facade.m_userProjectsRepository
                        .findAllByUserId(fromString(userId)), UserProjects::getProjectId)),
                "ProjectServiceHelper::findProjectsByUserId");
    }

    /**
     * Save project participant by user id and project id.
     *
     * @param userId    represent the user id.
     * @param projectId represent the project id.
     */
    public ProjectParticipants saveParticipantToProject(UUID userId, UUID projectId)
    {
        return doForRepository(() -> m_facade.m_projectParticipantsRepository.save(new ProjectParticipants(userId, projectId)),
                "ProjectServiceHelper::saveParticipantsToProject");
    }

    /**
     * Save All Project Participants with project id and user ids.
     *
     * @param projectId represent the project id.
     * @return Iterable<ProjectParticipants>
     */
    public Iterable<ProjectParticipants> saveParticipantToProject(List<UUID> userIds, UUID projectId)
    {
        var participants = toList(userIds, userId -> new ProjectParticipants(userId, projectId));

        return doForRepository(() -> m_facade.m_projectParticipantsRepository.saveAll(participants),
                "ProjectServiceHelper::saveParticipantsToProject");
    }
}