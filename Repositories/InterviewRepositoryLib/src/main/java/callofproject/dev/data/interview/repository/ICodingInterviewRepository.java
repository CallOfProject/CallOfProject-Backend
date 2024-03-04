package callofproject.dev.data.interview.repository;


import callofproject.dev.data.interview.entity.CodingInterview;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface ICodingInterviewRepository extends CrudRepository<CodingInterview, UUID>
{
}
