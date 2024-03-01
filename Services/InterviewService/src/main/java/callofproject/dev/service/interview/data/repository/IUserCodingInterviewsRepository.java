package callofproject.dev.service.interview.data.repository;

import callofproject.dev.service.interview.data.entity.UserCodingInterviews;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IUserCodingInterviewsRepository extends CrudRepository<UserCodingInterviews, UUID>
{
    @Query("from UserCodingInterviews uci where uci.m_codingInterview.m_codingInterviewId = :codingInterviewId")
    Iterable<UserCodingInterviews> findParticipantsByCodingInterview_Id(UUID codingInterviewId);

    @Query("from UserCodingInterviews uci where uci.m_user.m_userId = :userId and uci.m_codingInterview.m_codingInterviewId = :codeInterviewId")
    UserCodingInterviews findUserCodingInterviewsByUserIdAndCodingInterviewId(UUID userId, UUID codeInterviewId);
}
