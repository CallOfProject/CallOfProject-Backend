package callofproject.dev.data.interview.repository;


import callofproject.dev.data.interview.entity.UserTestInterviews;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Lazy
public interface IUserTestInterviewsRepository extends CrudRepository<UserTestInterviews, UUID>
{
    @Query("from UserTestInterviews as uti where uti.m_user.m_userId = :userId and uti.m_testInterview.m_id = :interviewId")
    Optional<UserTestInterviews> findUserTestInterviewsByUserAndTestInterviewId(UUID userId, UUID interviewId);

    @Query("from UserTestInterviews where m_testInterview.m_id = :testInterviewId")
    Optional<UserTestInterviews> findUserTestInterviewsByTestInterviewId(UUID testInterviewId);
}
