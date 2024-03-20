package callofproject.dev.task.config;

import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Task Filter Specifications
 * <p>
 * This class contains the task filter specifications
 */
public class TaskFilterSpecifications
{
    /**
     * Has priority specification
     *
     * @param priority the priority
     * @return the specification
     */
    public static Specification<Task> hasPriority(Priority priority)
    {
        return (root, query, criteriaBuilder) ->
                priority == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_priority"), priority);
    }

    /**
     * Has task status specification
     *
     * @param taskStatus the task status
     * @return the specification
     */
    public static Specification<Task> hasTaskStatus(TaskStatus taskStatus)
    {
        return (root, query, criteriaBuilder) ->
                taskStatus == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_taskStatus"), taskStatus);
    }

    /**
     * Has start date specification
     *
     * @param startDate the start date
     * @return the specification
     */
    public static Specification<Task> hasStartDate(LocalDate startDate)
    {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_startDate"), startDate);
    }

    /**
     * Has finish date specification
     *
     * @param finishDate the finish date
     * @return the specification
     */
    public static Specification<Task> hasFinishDate(LocalDate finishDate)
    {
        return (root, query, criteriaBuilder) ->
                finishDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_endDate"), finishDate);
    }

    /**
     * Has project id specification
     *
     * @param projectId the project id
     * @return the specification
     */
    public static Specification<Task> hasProjectId(UUID projectId)
    {
        return (root, query, criteriaBuilder) -> projectId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_project").get("m_projectId"), projectId);
    }

    /**
     * Has project owner id specification
     *
     * @param projectOwnerId the project owner id
     * @return the specification
     */
    public static Specification<Task> hasProjectOwnerId(UUID projectOwnerId)
    {
        return (root, query, criteriaBuilder) ->
                projectOwnerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_project").get("m_projectOwner").get("m_userId"), projectOwnerId);
    }
}
