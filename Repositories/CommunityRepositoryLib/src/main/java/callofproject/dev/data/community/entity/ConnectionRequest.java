package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "connection_request")
public class ConnectionRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "connection_request_id")
    private UUID m_connectionRequestId;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    private User m_requester;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    private User m_requestee;

    public ConnectionRequest()
    {
    }


    public ConnectionRequest(User requester, User requestee)
    {
        this.m_requester = requester;
        this.m_requestee = requestee;
    }


    public UUID getConnectionRequestId()
    {
        return m_connectionRequestId;
    }

    public void setConnectionRequestId(UUID connectionRequestId)
    {
        this.m_connectionRequestId = connectionRequestId;
    }

    public User getRequester()
    {
        return m_requester;
    }

    public void setRequester(User requester)
    {
        this.m_requester = requester;
    }

    public User getRequestee()
    {
        return m_requestee;
    }

    public void setRequestee(User requestee)
    {
        this.m_requestee = requestee;
    }
}
