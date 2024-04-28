package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "user_connection")
public class UserConnection
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_connection_id")
    private UUID m_userConnectionId;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    private User m_mainUser;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    private User m_connectedUser;

    public UserConnection()
    {
    }

    public UserConnection(User mainUser, User connectedUser)
    {
        m_mainUser = mainUser;
        m_connectedUser = connectedUser;
    }

    public UUID getUserConnectionId()
    {
        return m_userConnectionId;
    }

    public void setUserConnectionId(UUID userConnectionId)
    {
        m_userConnectionId = userConnectionId;
    }

    public User getMainUser()
    {
        return m_mainUser;
    }

    public void setMainUser(User mainUser)
    {
        m_mainUser = mainUser;
    }

    public User getConnectedUser()
    {
        return m_connectedUser;
    }

    public void setConnectedUser(User connectedUser)
    {
        m_connectedUser = connectedUser;
    }
}
