package callofproject.dev.repository.environment.repository;

import callofproject.dev.repository.environment.entity.University;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.repository.environment.BeanName.UNIVERSITY_REPOSITORY;

@Repository(UNIVERSITY_REPOSITORY)
@Lazy
public interface IUniversityRepository extends MongoRepository<University, String>
{
    boolean existsByUniversityNameIgnoreCase(String universityName);

    Optional<University> findByUniversityNameIgnoreCase(String universityName);

    Iterable<University> findAllByUniversityNameContainingIgnoreCase(String name);
}
