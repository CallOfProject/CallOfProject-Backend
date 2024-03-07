package callofproject.dev.data.interview.repository;


import callofproject.dev.data.interview.entity.CodingInterview;
import callofproject.dev.data.interview.entity.TestInterview;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@Lazy
public interface ITestInterviewRepository extends CrudRepository<TestInterview, UUID>
{
    @Query("from TestInterview where " +
            "YEAR(m_endTime) = YEAR(:endDate) and " +
            "MONTH(m_endTime) = MONTH(:endDate) and " +
            "DAY(m_endTime) = DAY(:endDate) and (" +
            "m_interviewStatus = 'SCHEDULED' or " +
            "m_interviewStatus = 'NOT_STARTED')")
    Iterable<TestInterview> findAllTestInterviewsByEnDate(LocalDateTime endDate);
}
