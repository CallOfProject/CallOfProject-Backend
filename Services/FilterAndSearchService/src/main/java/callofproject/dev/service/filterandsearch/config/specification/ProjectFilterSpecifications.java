package callofproject.dev.service.filterandsearch.config.specification;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.enums.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * This class contains specifications for filtering projects.
 */
public final class ProjectFilterSpecifications
{
    /**
     * This constructor is used to prevent instantiation of this class.
     */
    private ProjectFilterSpecifications()
    {
    }

    /**
     * This method creates a specification for searching projects by a keyword.
     *
     * @param keyword The keyword to search for.
     * @return The specification for searching projects by a keyword.
     */
    public static Specification<Project> searchProjects(String keyword)
    {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank())
                return criteriaBuilder.conjunction();
            else
            {
                String pattern = "%" + keyword.toLowerCase() + "%";
                var usernamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_projectName")), pattern);
                var firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_projectSummary")), pattern);
                var middleNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_technicalRequirements")), pattern);
                var lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_specialRequirements")), pattern);

                return criteriaBuilder.or(usernamePredicate, firstNamePredicate, middleNamePredicate, lastNamePredicate);
            }
        };
    }


    /**
     * This method creates a specification for filtering projects by a project owner's username.
     *
     * @param professionLevel The profession level to filter by.
     * @return The specification for filtering projects by a project owner's username.
     */
    public static Specification<Project> filterByProfessionalLevel(EProjectProfessionLevel professionLevel)
    {
        return (root, query, criteriaBuilder) ->
                professionLevel == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_professionLevel"), professionLevel);
    }

    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param projectLevel The project level to filter by.
     * @return The specification for filtering projects by a project level.
     */
    public static Specification<Project> filterByProjectLevel(EProjectLevel projectLevel)
    {
        return (root, query, criteriaBuilder) ->
                projectLevel == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_projectLevel"), projectLevel);
    }

    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param degree The degree to filter by.
     * @return The specification for filtering projects by a degree.
     */
    public static Specification<Project> filterByDegree(EDegree degree)
    {
        return (root, query, criteriaBuilder) ->
                degree == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_degree"), degree);
    }

    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param feedbackTimeRange The feedback time range to filter by.
     * @return The specification for filtering projects by a feedback time range.
     */
    public static Specification<Project> filterByFeedbackTimeRange(EFeedbackTimeRange feedbackTimeRange)
    {
        return (root, query, criteriaBuilder) ->
                feedbackTimeRange == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_feedbackTimeRange"), feedbackTimeRange);
    }

    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param interviewType The interview type to filter by.
     * @return The specification for filtering projects by an interview type.
     */
    public static Specification<Project> filterByInterviewType(EInterviewType interviewType)
    {
        return (root, query, criteriaBuilder) ->
                interviewType == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_interviewType"), interviewType);
    }

    public static Specification<Project> filterByProjectStatus(EProjectStatus projectStatus)
    {
        return (root, query, criteriaBuilder) ->
                projectStatus == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_projectStatus"), projectStatus);
    }

    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param startDate The start date to filter by.
     * @return The specification for filtering projects by a start date.
     */
    public static Specification<Project> filterByStartDate(LocalDate startDate)
    {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_startDate"), startDate);
    }

    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param completionDate The completion date to filter by.
     * @return The specification for filtering projects by a completion date.
     */
    public static Specification<Project> filterByExpectedCompletionDate(LocalDate completionDate)
    {
        return (root, query, criteriaBuilder) ->
                completionDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_expectedCompletionDate"), completionDate);
    }


    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param applicationDeadline The application deadline to filter by.
     * @return The specification for filtering projects by an application deadline.
     */
    public static Specification<Project> filterByApplicationDeadline(LocalDate applicationDeadline)
    {
        return (root, query, criteriaBuilder) ->
                applicationDeadline == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_applicationDeadline"), applicationDeadline);
    }


    /**
     * This method creates a specification for filtering projects by a project owner's profession level.
     *
     * @param keyword The keyword to filter by.
     * @return The specification for filtering projects by a keyword.
     */
    public static Specification<Project> filterByKeyword(String keyword)
    {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank())
                return criteriaBuilder.conjunction();
            else
            {
                String pattern = "%" + keyword.toLowerCase() + "%";
                var projectNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_projectName")), pattern);
                var summaryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_projectSummary")), pattern);
                var descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_description")), pattern);
                var aimPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_projectAim")), pattern);
                var technicalReqPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_technicalRequirements")), pattern);
                var specialReqPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("m_specialRequirements")), pattern);

                return criteriaBuilder.or(projectNamePredicate, summaryPredicate, descriptionPredicate, aimPredicate, technicalReqPredicate, specialReqPredicate);
            }
        };
    }
}
