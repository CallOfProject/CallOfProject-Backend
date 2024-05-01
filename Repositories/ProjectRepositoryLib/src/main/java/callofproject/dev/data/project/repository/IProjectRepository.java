package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.Project;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_REPOSITORY;

@Repository(PROJECT_REPOSITORY)
@Lazy
public interface IProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project>
{
    @Query("from Project where m_projectOwner.m_userId = :userId")
    Page<Project> findAllByProjectOwnerId(UUID userId, Pageable pageable);

    @Query("FROM Project p WHERE :userId IN (select w.m_user.m_userId FROM p.m_projectParticipants as w)")
    Page<Project> findAllParticipantProjectByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
            from Project where (m_projectStatus = 'NOT_STARTED' or m_projectStatus = 'IN_PROGRESS')
            and m_adminOperationStatus = 'ACTIVE'
            and m_projectAccessType = 'PUBLIC'
            and m_deletedAt is null
            order by m_creationDate desc
            """)
    Page<Project> findAllByProjectStatusAndAdminOperationStatusAndProjectAccessType(Pageable pageable);

    @Query("from Project where m_projectStatus = 'EXTEND_APPLICATION_FEEDBACK' and m_adminOperationStatus = 'ACTIVE'")
    List<Project> findAllExtendedDateApplications();

    Page<Project> findAllByApplicationDeadline(LocalDate date, Pageable pageable);

    @Query("from Project where m_applicationDeadline = :date and (m_projectStatus = 'IN_PROGRESS' or m_projectStatus = 'NOT_STARTED') and m_deletedAt is null")
    List<Project> findAllByApplicationDeadline(LocalDate date);

    @Query("from Project where m_expectedCompletionDate = :date and (m_projectStatus = 'IN_PROGRESS' or m_projectStatus = 'NOT_STARTED') and m_deletedAt is null")
    Page<Project> findAllByExpectedCompletionDate(LocalDate date, Pageable pageable);

    @Query("from Project where m_expectedCompletionDate = :date and (m_projectStatus = 'IN_PROGRESS' or m_projectStatus = 'NOT_STARTED') and m_deletedAt is null")
    Iterable<Project> findAllByExpectedCompletionDate(LocalDate date);

    @Query("from Project where m_startDate = :date and m_projectStatus = 'NOT_STARTED'")
    Iterable<Project> findAllByStartDate(LocalDate date);
}