package callofproject.dev.authentication.repository;

import callofproject.dev.authentication.entity.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITokenRepository extends CrudRepository<Token, Long>
{
    Optional<Token> findByToken(String token);

    @Query("from Token where userId = :p_userId and expired = false and revoked = false")
    Iterable<Token> findAllValidTokenByUserId(UUID p_userId);
}
