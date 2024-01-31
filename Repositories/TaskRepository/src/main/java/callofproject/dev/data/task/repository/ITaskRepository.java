package callofproject.dev.data.task.repository;

import callofproject.dev.data.task.entity.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
@Lazy
public interface ITaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task>
{
    @Query("from Task where m_project.m_projectId = :projectId")
    Page<Task> findAllByProjectId(UUID projectId, Pageable pageable);

    @Query("from Task where m_startDate = :startDate and m_endDate = :endDate")
    Page<Task> findAllByStartDateAndEndDate(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("from Task where m_startDate = :startDate")
    Page<Task> findAllByStartDate(LocalDate startDate, Pageable pageable);

    @Query("from Task where m_startDate > :startDate")
    Page<Task> findTasksByStartDateAfter(LocalDate startDate, Pageable pageable);

    @Query("from Task where m_startDate < :startDate")
    Page<Task> findTasksByStartDateBefore(LocalDate startDate, Pageable pageable);

    @Query("from Task where m_endDate = :endDate")
    Page<Task> findTasksByEndDate(LocalDate endDate, Pageable pageable);

    @Query("from Task where m_endDate > :endDate")
    Page<Task> findTasksByEndDateAfter(LocalDate endDate, Pageable pageable);

    @Query("from Task where m_endDate < :endDate")
    Page<Task> findTasksByEndDateBefore(LocalDate endDate, Pageable pageable);

    @Query("from Task where m_startDate between :startDate and :endDate")
    Page<Task> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("from Task where m_endDate between :startDate and :endDate")
    Page<Task> findAllByEndDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("from Task where m_priority = :priority")
    Page<Task> findAllByPriority(String priority, Pageable pageable);

    @Query("from Task where m_taskStatus = :taskStatus")
    Page<Task> findAllByTaskStatus(String taskStatus, Pageable pageable);

    @Query("from Task where m_taskStatus = :taskStatus and m_project.m_projectId = :projectId")
    Page<Task> findAllByTaskStatusAndProjectProjectId(String taskStatus, UUID projectId, Pageable pageable);

    @Query("from Task where m_priority = :priority and m_project.m_projectId = :projectId")
    Page<Task> findAllByPriorityAndProjectProjectId(String priority, UUID projectId, Pageable pageable);

    //Page<Task> findAllTasksByFilter(Specification<Task> spec, Pageable pageable);

    @Override
    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}
