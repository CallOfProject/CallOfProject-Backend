package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_connections")
public class UserConnection
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "connection_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "connected_user_id")
    private User connectedUser;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserConnection()
    {
    }

    public UserConnection(User user, User connectedUser)
    {
        this.user = user;
        this.connectedUser = connectedUser;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public User getConnectedUser()
    {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser)
    {
        this.connectedUser = connectedUser;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

}