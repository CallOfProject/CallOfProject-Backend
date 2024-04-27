package callofproject.dev.service.filterandsearch.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.project.repository.IProjectRepository;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import callofproject.dev.service.filterandsearch.dto.ProjectDTO;
import callofproject.dev.service.filterandsearch.dto.ProjectFilterDTO;
import callofproject.dev.service.filterandsearch.dto.ProjectsDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static callofproject.dev.service.filterandsearch.config.specification.ProjectFilterSpecifications.*;
import static java.lang.String.format;

/**
 * FilterService
 */
@Service
@Lazy
@Transactional(transactionManager = "projectDbTransactionManager")
public class FilterService
{
    private final IUserRepository m_userRepository;
    private final IProjectRepository m_projectRepository;

    /**
     * Constructor
     *
     * @param userRepository    IUserRepository
     * @param projectRepository IProjectRepository
     */
    public FilterService(IUserRepository userRepository, IProjectRepository projectRepository)
    {
        m_userRepository = userRepository;
        m_projectRepository = projectRepository;
    }

    /**
     * Filter projects
     *
     * @param dto  ProjectFilterDTO
     * @param page int
     * @return Filtered projects
     */
    public MultipleResponseMessagePageable<Object> filterProjects(ProjectFilterDTO dto, int page)
    {
        var spec = Specification
                .where(filterByProfessionalLevel(dto.professionLevel()))
                .and(filterByProjectLevel(dto.projectLevel()))
                .and(filterByDegree(dto.degree()))
                .and(filterByFeedbackTimeRange(dto.feedbackTimeRange()))
                .and(filterByInterviewType(dto.interviewType()))
                .and(filterByProjectStatus(dto.projectStatus()))
                .and(filterByStartDate(dto.startDate()))
                .and(filterByExpectedCompletionDate(dto.expectedCompletionDate()))
                .and(filterByApplicationDeadline(dto.applicationDeadline()))
                .and(filterByKeyword(dto.keyword()));

        var pageRequest = PageRequest.of(page - 1, 20);
        var projectPage = m_projectRepository.findAll(spec, pageRequest);
        var projectsDTO = new ProjectsDTO(projectPage.get().map(p -> new ProjectDTO(p.getProjectId(), p.getProjectName(), p.getProjectImagePath(), p.getProjectSummary(), p.getProjectOwner().getUsername(), p.getProjectStatus())).toList());
        var elementCount = projectsDTO.projects().size();
        return new MultipleResponseMessagePageable<>(projectPage.getTotalPages(), page, elementCount, format("%d projects are found!", elementCount), projectsDTO);
    }
}
