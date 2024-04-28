package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.UserConnection;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IUserConnectionRepository extends CrudRepository<UserConnection, UUID>
{
    @Query("from UserConnection where m_mainUser.m_userId = :userId")
    Iterable<UserConnection> findUserConnectionsByUserId(UUID userId);
}
