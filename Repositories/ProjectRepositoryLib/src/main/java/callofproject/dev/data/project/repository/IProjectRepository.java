package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.enums.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_REPOSITORY;

@Repository(PROJECT_REPOSITORY)
@Lazy
public interface IProjectRepository extends JpaRepository<Project, UUID>
{

    Page<Project> findAllByProjectName(String projectName, Pageable pageable);

    Page<Project> findAllByProjectNameContaining(String word, Pageable pageable);

    Page<Project> findAllByProjectNameContainingIgnoreCase(String word, Pageable pageable);

    Page<Project> findAllByDescriptionContainingIgnoreCase(String word, Pageable pageable);

    Page<Project> findAllByProjectSummaryContainingIgnoreCase(String word, Pageable pageable);

    Page<Project> findAllByProjectAimContainsIgnoreCase(String word, Pageable pageable);

    Page<Project> findAllByApplicationDeadline(LocalDate date, Pageable pageable);

    Page<Project> findAllByApplicationDeadlineAfter(LocalDate date, Pageable pageable);

    Page<Project> findAllByApplicationDeadlineBefore(LocalDate date, Pageable pageable);

    @Query("from Project where m_applicationDeadline < :date")
    List<Project> findAllByApplicationDeadlineBefore(LocalDate date);

    Page<Project> findAllByApplicationDeadlineBetween(LocalDate start, LocalDate end, Pageable pageable);

    Page<Project> findAllByExpectedCompletionDate(LocalDate date, Pageable pageable);

    Page<Project> findAllByExpectedCompletionDateAfter(LocalDate date, Pageable pageable);

    Page<Project> findAllByExpectedCompletionDateBefore(LocalDate date, Pageable pageable);

    Page<Project> findAllByExpectedCompletionDateBetween(LocalDate start, LocalDate end, Pageable pageable);

/*    Page<Project> findAllByExpectedProjectDeadline(LocalDate date, Pageable pageable);

    Page<Project> findAllByExpectedProjectDeadlineBetween(LocalDate start, LocalDate end, Pageable pageable);

    Page<Project> findAllByExpectedProjectDeadlineAfter(LocalDate date, Pageable pageable);

    Page<Project> findAllByExpectedProjectDeadlineBefore(LocalDate date, Pageable pageable);*/

    Page<Project> findAllByMaxParticipant(int maxParticipant, Pageable pageable);

    Page<Project> findAllByMaxParticipantLessThanEqual(int maxParticipant, Pageable pageable);

    Page<Project> findAllByMaxParticipantGreaterThanEqual(int minParticipant, Pageable pageable);

    Page<Project> findAllByInviteLink(String link, Pageable pageable);

    @Query("FROM Project WHERE m_projectAccessType = :accessType")
    Page<Project> findAllByProjectAccessType(@Param("accessType") EProjectAccessType accessType, Pageable pageable);


    @Query("FROM Project WHERE m_professionLevel = :professionLevel")
    Page<Project> findAllByProfessionLevel(@Param("professionLevel") EProjectProfessionLevel professionLevel, Pageable pageable);

    @Query("FROM Project WHERE m_sector = :sector")
    Page<Project> findAllBySector(@Param("sector") ESector sector, Pageable pageable);

    @Query("FROM Project WHERE m_degree = :degree")
    Page<Project> findAllByDegree(@Param("degree") EDegree degree, Pageable pageable);

    @Query("FROM Project WHERE m_projectLevel = :projectLevel")
    Page<Project> findAllByProjectLevel(@Param("projectLevel") EProjectLevel projectLevel, Pageable pageable);

    @Query("FROM Project WHERE m_interviewType = :interviewType")
    Page<Project> findAllByInterviewType(@Param("interviewType") EInterviewType interviewType, Pageable pageable);

    Page<Project> findAllByProjectNameAndDescriptionAndProjectSummaryAndProjectAimContainsIgnoreCase(String projectName, String description, String projectSummary, String projectAim, Pageable pageable);

    Page<Project> findAllByProjectNameOrDescriptionOrProjectSummaryOrProjectAimContainsIgnoreCase(String projectName, String description, String projectSummary, String projectAim, Pageable pageable);

    @Query("from Project where m_projectOwner.m_username = :username")
    Page<Project> findAllByProjectOwnerUsername(String username, Pageable pageable);

    @Query("from Project where m_projectOwner.m_userId = :userId")
    Page<Project> findAllByProjectOwnerId(UUID userId, Pageable pageable);

    @Query("from Project where m_projectOwner.m_userId = :userId")
    Iterable<Project> findAllByProjectOwnerId(UUID userId);

    @Query("FROM Project p WHERE :userId IN (select w.m_user.m_userId FROM p.m_projectParticipants as w)")
    Page<Project> findAllParticipantProjectByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("FROM Project p WHERE :projectId IN (select w.m_project.m_projectId FROM p.m_projectParticipantRequests as w)")
    Iterable<Project> findAllByProjectProjectId(@Param("projectId") UUID projectId);

    @Query("FROM Project p WHERE :userId IN (select w.m_user.m_userId FROM p.m_projectParticipantRequests as w)")
    Iterable<Project> findAllByUserUserId(UUID userId);

    @Query("""
            from Project where (m_projectStatus = 'NOT_STARTED' or m_projectStatus = 'IN_PROGRESS')
            and m_adminOperationStatus = 'ACTIVE'
            and m_projectAccessType = 'PUBLIC'
            """)
    Page<Project> findAllByProjectStatusAndAdminOperationStatusAndProjectAccessType(Pageable pageable);
}