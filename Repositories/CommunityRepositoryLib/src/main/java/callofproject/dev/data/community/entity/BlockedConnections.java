package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blocked_connections")
public class BlockedConnections
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // UUID için AUTO veya özel bir strateji kullanılmalı.
    @Column(name = "block_id")
    private UUID m_blockedId;

    @ManyToOne
    @JoinColumn(name = "blocker_id")
    private User blocker;

    @ManyToOne
    @JoinColumn(name = "blocked_id")
    private User blocked;


    public BlockedConnections()
    {
    }

    public BlockedConnections(User blocker, User blocked)
    {
        this.blocker = blocker;
        this.blocked = blocked;
    }

    public UUID getBlockedId()
    {
        return m_blockedId;
    }

    public void setBlockedId(UUID blockedId)
    {
        m_blockedId = blockedId;
    }

    public User getBlocker()
    {
        return blocker;
    }

    public void setBlocker(User blocker)
    {
        this.blocker = blocker;
    }

    public User getBlocked()
    {
        return blocked;
    }

    public void setBlocked(User blocked)
    {
        this.blocked = blocked;
    }
}
