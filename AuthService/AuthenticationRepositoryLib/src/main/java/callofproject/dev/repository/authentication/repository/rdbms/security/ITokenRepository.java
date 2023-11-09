package callofproject.dev.repository.authentication.repository.rdbms.security;

import callofproject.dev.repository.authentication.entity.security.Token;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static callofproject.dev.repository.authentication.BeanName.TOKEN_REPOSITORY_BEAN;

@Repository(TOKEN_REPOSITORY_BEAN)
@Lazy
public interface ITokenRepository extends CrudRepository<Token, Long>
{
    Optional<Token> findByToken(String token);

    @Query("from Token where username = :p_username and expired = false and revoked = false")
    List<Token> findAllValidTokenByUserUsername(String p_username);

    void deleteTokenByToken(String token);

    Iterable<Token> findTokensByExpired(boolean expired);

    Iterable<Token> deleteTokensByExpired(boolean isExpired);
}
