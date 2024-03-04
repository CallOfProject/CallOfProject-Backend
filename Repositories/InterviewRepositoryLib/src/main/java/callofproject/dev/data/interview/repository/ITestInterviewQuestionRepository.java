package callofproject.dev.data.interview.repository;


import callofproject.dev.data.interview.entity.TestInterviewQuestion;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface ITestInterviewQuestionRepository extends CrudRepository<TestInterviewQuestion, Long>
{
}
