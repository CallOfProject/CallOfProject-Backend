package callofproject.dev.authentication.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "token")
public class Token
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @Column(name = "user_id")
    public UUID userId;

    public String token;

    public boolean revoked;

    public boolean expired;

    public Token()
    {

    }

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

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;*/
}
