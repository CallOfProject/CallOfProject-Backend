package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "block_connection")
public class BlockConnection
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "block_connection_id")
    private UUID m_blockConnectionId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private User m_mainUser;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private User m_blockedUser;

    public BlockConnection()
    {
    }

    public BlockConnection(User mainUser, User blockedUser)
    {
        m_mainUser = mainUser;
        m_blockedUser = blockedUser;
    }

    public UUID getBlockConnectionId()
    {
        return m_blockConnectionId;
    }

    public void setBlockConnectionId(UUID blockConnectionId)
    {
        m_blockConnectionId = blockConnectionId;
    }

    public User getMainUser()
    {
        return m_mainUser;
    }

    public void setMainUser(User mainUser)
    {
        m_mainUser = mainUser;
    }

    public User getBlockedUser()
    {
        return m_blockedUser;
    }

    public void setBlockedUser(User blockedUser)
    {
        m_blockedUser = blockedUser;
    }
}
