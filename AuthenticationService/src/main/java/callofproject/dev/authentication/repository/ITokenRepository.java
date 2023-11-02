package callofproject.dev.authentication.repository;



import callofproject.dev.authentication.entity.Token;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("callofproject.dev.authentication.repository.token")
@Lazy
public interface ITokenRepository extends CrudRepository<Token, Long>
{
    Optional<Token> findByToken(String token);

    @Query("from Token where userId = :p_userId and expired = false and revoked = false")
    List<Token> findAllValidTokenByUserId(UUID p_userId);
}
