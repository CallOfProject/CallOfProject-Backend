package callofproject.dev.service.interview.data.repository;

import callofproject.dev.service.interview.data.QuestionAnswer;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IQuestionAnswerRepository extends CrudRepository<QuestionAnswer, Long>
{
}
