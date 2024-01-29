package callofproject.dev.data.task.repository;

import callofproject.dev.data.task.entity.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
@Lazy
public interface ITaskRepository extends JpaRepository<Task, UUID>
{
    @Query("from Task where m_project.m_projectId = :projectId")
    Iterable<Task> findAllByProjectId(UUID projectId);

    @Query("from Task where m_startDate = :startDate and m_endDate = :endDate")
    Iterable<Task> findAllByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);


    @Query("from Task where m_startDate = :startDate")
    Iterable<Task> findAllByStartDate(LocalDate startDate);

    @Query("from Task where m_startDate > :startDate")
    Iterable<Task> findTasksByStartDateAfter(LocalDate startDate);

    @Query("from Task where m_startDate < :startDate")
    Iterable<Task> findTasksByStartDateBefore(LocalDate startDate);

    @Query("from Task where m_endDate = :endDate")
    Iterable<Task> findTasksByEndDate(LocalDate endDate);

    @Query("from Task where m_endDate > :endDate")
    Iterable<Task> findTasksByEndDateAfter(LocalDate endDate);

    @Query("from Task where m_endDate < :endDate")
    Iterable<Task> findTasksByEndDateBefore(LocalDate endDate);

    @Query("from Task where m_startDate between :startDate and :endDate")
    Iterable<Task> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("from Task where m_endDate between :startDate and :endDate")
    Iterable<Task> findAllByEndDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("from Task where m_priority = :priority")
    Iterable<Task> findAllByPriority(String priority);

    @Query("from Task where m_taskStatus = :taskStatus")
    Iterable<Task> findAllByTaskStatus(String taskStatus);

    @Query("from Task where m_taskStatus = :taskStatus and m_project.m_projectId = :projectId")
    Iterable<Task> findAllByTaskStatusAndProjectProjectId(String taskStatus, UUID projectId);

    @Query("from Task where m_priority = :priority and m_project.m_projectId = :projectId")
    Iterable<Task> findAllByPriorityAndProjectProjectId(String priority, UUID projectId);
}
