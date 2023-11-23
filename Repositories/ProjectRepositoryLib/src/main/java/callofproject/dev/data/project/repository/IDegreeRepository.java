package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.Degree;
import callofproject.dev.data.project.entity.enums.EDegree;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.DEGREE_REPOSITORY;

@Repository(DEGREE_REPOSITORY)
@Lazy
public interface IDegreeRepository extends CrudRepository<Degree, Long>
{
    @Query("SELECT d FROM Degree d WHERE d.m_degree = :degree")
    Optional<Degree> findDegreeByDegree(EDegree degree);
}
