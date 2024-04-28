package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.BlockConnection;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IBlockConnectionRepository extends CrudRepository<BlockConnection, UUID>
{
    @Query("from BlockConnection where m_mainUser.m_userId = :requesterId")
    Iterable<BlockConnection> findBlockedConnectionsByUserId(UUID requesterId);
}
