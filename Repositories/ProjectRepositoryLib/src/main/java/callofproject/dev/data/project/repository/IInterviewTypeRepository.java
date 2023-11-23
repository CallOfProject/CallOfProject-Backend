package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.InterviewType;
import callofproject.dev.data.project.entity.enums.EInterviewType;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.INTERVIEW_TYPE_REPOSITORY;

@Repository(INTERVIEW_TYPE_REPOSITORY)
@Lazy
public interface IInterviewTypeRepository extends CrudRepository<InterviewType, Long>
{
    @Query("SELECT it FROM InterviewType it WHERE it.m_interviewType = :interviewType")
    Optional<InterviewType> findInterviewTypeByInterviewType(EInterviewType interviewType);
}
