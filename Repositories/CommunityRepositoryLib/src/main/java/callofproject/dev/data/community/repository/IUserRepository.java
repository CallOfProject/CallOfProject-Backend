package callofproject.dev.data.community.repository;


import callofproject.dev.data.community.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Lazy
public interface IUserRepository extends CrudRepository<User, UUID>
{
    @Query("from User where m_username = :username")
    Optional<User> findByUsername(String username);
}
