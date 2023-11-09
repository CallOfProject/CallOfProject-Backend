package callofproject.dev.repository.authentication.entity.security;

import jakarta.persistence.*;

@Entity
@Table(name = "token")
public class Token
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @Column(name = "user_id")
    public String username;

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

    public Token(String username, String token, boolean revoked, boolean expired)
    {
        this.username = username;
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
    }

    public String getUserId()
    {
        return username;
    }

    public void setUserId(String username)
    {
        this.username = username;
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
