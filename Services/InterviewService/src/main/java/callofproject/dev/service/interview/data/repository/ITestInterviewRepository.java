package callofproject.dev.service.interview.data.repository;

import callofproject.dev.service.interview.data.entity.TestInterview;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface ITestInterviewRepository extends CrudRepository<TestInterview, UUID>
{
}
