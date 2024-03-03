package callofproject.dev.service.interview.data.repository;

import callofproject.dev.service.interview.data.entity.UserTestInterviews;
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
    @Query("from UserTestInterviews where m_user.m_userId = :userId and m_testInterview.m_id = :id")
    Optional<UserTestInterviews> findUserTestInterviewsByUserAndTestInterviewId(UUID userId, UUID id);
}
