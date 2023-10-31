package callofproject.dev.authentication.entity;

import callofproject.dev.repository.usermanagement.entity.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Token
{

    @Id
    @GeneratedValue
    public Long id;
    @Column(name = "user_id")
    public UUID userId;

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    public Token(String token, boolean revoked, boolean expired)
    {
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
    }

    public Token(UUID userId, String token, boolean revoked, boolean expired)
    {
        this.userId = userId;
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public boolean isRevoked()
    {
        return revoked;
    }

    public void setRevoked(boolean revoked)
    {
        this.revoked = revoked;
    }

    public boolean isExpired()
    {
        return expired;
    }

    public void setExpired(boolean expired)
    {
        this.expired = expired;
    }
    /*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;*/
}
