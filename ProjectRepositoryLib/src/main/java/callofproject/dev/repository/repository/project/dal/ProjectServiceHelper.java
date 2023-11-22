package callofproject.dev.repository.repository.project.dal;

import callofproject.dev.repository.repository.project.entity.Project;
import callofproject.dev.repository.repository.project.entity.enums.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
public class ProjectServiceHelper
{
    private final RepositoryFacade m_facade;

    public ProjectServiceHelper(RepositoryFacade facade)
    {
        m_facade = facade;
    }

    public Project saveProject(Project project)
    {
        return doForRepository(() -> m_facade.m_projectRepository.save(project),
                "ProjectServiceHelper::saveProject");
    }

    public Iterable<Project> findAllProjectsPageable(int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAll(pageable),
                "ProjectServiceHelper::findAllProjects");
    }


    public Iterable<Project> findAllProjectsByProjectName(String projectName, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectName(projectName, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectName");
    }

    public Iterable<Project> findAllProjectsByProjectNameContaining(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectNameContaining(word, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectNameContaining");
    }

    public Iterable<Project> findAllProjectsByDescriptionContainingIgnoreCase(String word, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByDescriptionContainingIgnoreCase(word, pageable),
                "ProjectServiceHelper::findAllProjectsByDescriptionContainingIgnoreCase");
    }

    public Iterable<Project> findAllProjectsByApplicationDeadline(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadline(date, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadline");
    }

    public Iterable<Project> findAllProjectsByApplicationDeadlineAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadlineAfter(date, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadlineAfter");
    }

    public Iterable<Project> findAllProjectsByApplicationDeadlineBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadlineBefore(date, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadlineBefore");
    }

    public Iterable<Project> findAllProjectsByApplicationDeadlineBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByApplicationDeadlineBetween(start, end, pageable),
                "ProjectServiceHelper::findAllProjectsByApplicationDeadlineBetween");
    }

    public Iterable<Project> findAllProjectsByExpectedCompletionDate(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDate(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDate");
    }

    public Iterable<Project> findAllProjectsByExpectedCompletionDateAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDateAfter(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDateAfter");
    }

    public Iterable<Project> findAllProjectsByExpectedCompletionDateBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDateBefore(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDateBefore");
    }

    public Iterable<Project> findAllProjectsByExpectedCompletionDateBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedCompletionDateBetween(start, end, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedCompletionDateBetween");
    }

    public Iterable<Project> findAllProjectsByExpectedProjectDeadline(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadline(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadline");
    }

    public Iterable<Project> findAllProjectsByExpectedProjectDeadlineAfter(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadlineAfter(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadlineAfter");
    }

    public Iterable<Project> findAllProjectsByExpectedProjectDeadlineBefore(LocalDate date, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadlineBefore(date, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadlineBefore");
    }

    public Iterable<Project> findAllProjectsByExpectedProjectDeadlineBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByExpectedProjectDeadlineBetween(start, end, pageable),
                "ProjectServiceHelper::findAllProjectsByExpectedProjectDeadlineBetween");
    }

    public Iterable<Project> findAllProjectsByMaxParticipant(int maxParticipant, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByMaxParticipant(maxParticipant, pageable),
                "ProjectServiceHelper::findAllProjectsByMaxParticipant");
    }

    public Iterable<Project> findAllProjectsByMaxParticipantLessThanEqual(int maxParticipant, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByMaxParticipantLessThanEqual(maxParticipant, pageable),
                "ProjectServiceHelper::findAllProjectsByMaxParticipantLessThanEqual");
    }

    public Iterable<Project> findAllProjectsByMaxParticipantGreaterThanEqual(int minParticipant, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByMaxParticipantGreaterThanEqual(minParticipant, pageable),
                "ProjectServiceHelper::findAllProjectsByMaxParticipantGreaterThanEqual");
    }

    public Iterable<Project> findAllProjectsByInviteLink(String link, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByInviteLink(link, pageable),
                "ProjectServiceHelper::findAllProjectsByInviteLink");
    }

    public Iterable<Project> findAllProjectsByProjectAccessType(EProjectAccessType accessType, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectAccessType(accessType, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectAccessType");
    }

    public Iterable<Project> findAllProjectsByProjectProfessionLevel(EProjectProfessionLevel professionLevel, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProfessionLevel(professionLevel, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectProfessionLevel");
    }

    public Iterable<Project> findAllProjectsBySector(ESector sector, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllBySector(sector, pageable),
                "ProjectServiceHelper::findAllProjectsBySector");
    }

    public Iterable<Project> findAllProjectsByDegree(EDegree degree, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByDegree(degree, pageable),
                "ProjectServiceHelper::findAllProjectsByDegree");
    }

    public Iterable<Project> findAllProjectsByProjectLevel(EProjectLevel projectLevel, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByProjectLevel(projectLevel, pageable),
                "ProjectServiceHelper::findAllProjectsByProjectLevel");
    }

    public Iterable<Project> findAllProjectsByInterviewType(EInterviewType interviewType, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository.findAllByInterviewType(interviewType, pageable),
                "ProjectServiceHelper::findAllProjectsByInterviewType");
    }

    public Iterable<Project> findAllProjectsByMultipleCriteria(String projectName, String description, String projectSummary, String projectAim, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository
                .findAllByProjectNameAndDescriptionAndProjectSummaryAndProjectAimContainsIgnoreCase(projectName, description,
                        projectSummary, projectAim, pageable), "ProjectServiceHelper::findAllProjectsByMultipleCriteria");
    }

    public Iterable<Project> findAllProjectsByAnyCriteria(String projectName, String description, String projectSummary, String projectAim, int page)
    {
        var pageable = PageRequest.of(page - 1, 10);

        return doForRepository(() -> m_facade.m_projectRepository
                .findAllByProjectNameOrDescriptionOrProjectSummaryOrProjectAimContainsIgnoreCase(projectName, description,
                        projectSummary, projectAim, pageable), "ProjectServiceHelper::findAllProjectsByAnyCriteria");
    }
}
