package callofproject.dev.repository.repository.project.repository;

import callofproject.dev.repository.repository.project.entity.Degree;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface IDegreeRepository extends CrudRepository<Degree, Long>
{
}
