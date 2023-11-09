package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.security.Token;
import callofproject.dev.repository.authentication.repository.rdbms.security.ITokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static callofproject.dev.repository.authentication.BeanName.TOKEN_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.TOKEN_REPOSITORY_BEAN;


@Component(TOKEN_DAL_BEAN)
@Lazy
public class TokenServiceHelper
{
    private final ITokenRepository m_tokenRepository;

    public TokenServiceHelper(@Qualifier(TOKEN_REPOSITORY_BEAN) ITokenRepository tokenRepository)
    {
        m_tokenRepository = tokenRepository;
    }

    public Token saveToken(Token token)
    {
        return m_tokenRepository.save(token);
    }

    public void removeTokenById(long tokenId)
    {
        m_tokenRepository.deleteById(tokenId);
    }

    public void removeToken(Token token)
    {
        m_tokenRepository.delete(token);
    }

    public Optional<Token> findByToken(String token)
    {
        return m_tokenRepository.findByToken(token);
    }

    public void deleteTokenByTokenStr(String token)
    {
        m_tokenRepository.deleteTokenByToken(token);
    }

    public Iterable<Token> removeAllTokensByExpired(boolean expired)
    {
        return m_tokenRepository.deleteTokensByExpired(expired);
    }

    public Iterable<Token> findTokensByExpired(boolean expired)
    {
        return m_tokenRepository.findTokensByExpired(expired);
    }

    public List<Token> findAllValidTokenByUsername(String username)
    {
        return m_tokenRepository.findAllValidTokenByUserUsername(username);
    }

    public Iterable<Token> saveAll(Iterable<Token> tokens)
    {
        return m_tokenRepository.saveAll(tokens);
    }
}
