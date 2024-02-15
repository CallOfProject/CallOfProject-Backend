package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.ConnectionRequests;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface IConnectionRequestsRepository extends JpaRepository<ConnectionRequests,UUID>
{
}
