package callofproject.dev.repository.authentication.entity.security;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "session")
public class Session
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID session_id;
    @Column
    private String token;
    @Column
    private LocalDate expireDate;
    @Column
    @CreationTimestamp
    private LocalDate creationDate;

    public Session()
    {

    }

    public UUID getSession_id()
    {
        return session_id;
    }

    public void setSession_id(UUID session_id)
    {
        this.session_id = session_id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public LocalDate getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate)
    {
        this.expireDate = expireDate;
    }

    public LocalDate getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate)
    {
        this.creationDate = creationDate;
    }
}
