package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.ConnectionRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IConnectionRequestRepository extends CrudRepository<ConnectionRequest, UUID>
{
    @Query("from ConnectionRequest where m_requestee.m_userId = :requesterId")
    Iterable<ConnectionRequest> findConnectionRequestsByRequesterId(UUID requesterId);
}
