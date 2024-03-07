package callofproject.dev.task.config;

import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class TaskFilterSpecifications
{
    public static Specification<Task> hasPriority(Priority priority)
    {
        return (root, query, criteriaBuilder) ->
                priority == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_priority"), priority);
    }

    public static Specification<Task> hasTaskStatus(TaskStatus taskStatus)
    {
        return (root, query, criteriaBuilder) ->
                taskStatus == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_taskStatus"), taskStatus);
    }

    public static Specification<Task> hasStartDate(LocalDate startDate)
    {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_startDate"), startDate);
    }

    public static Specification<Task> hasFinishDate(LocalDate finishDate)
    {
        return (root, query, criteriaBuilder) ->
                finishDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_endDate"), finishDate);
    }

    public static Specification<Task> hasProjectId(UUID projectId)
    {
        return (root, query, criteriaBuilder) -> projectId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_project").get("m_projectId"), projectId);
    }

    public static Specification<Task> hasProjectOwnerId(UUID projectOwnerId)
    {
        return (root, query, criteriaBuilder) ->
                projectOwnerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("m_project").get("m_projectOwner").get("m_userId"), projectOwnerId);
    }
}
