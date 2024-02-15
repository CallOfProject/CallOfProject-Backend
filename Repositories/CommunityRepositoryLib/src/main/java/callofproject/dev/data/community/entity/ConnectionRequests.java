package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "connection_requests")
public class ConnectionRequests
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request_id")
    private UUID m_requestId;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;


    @Enumerated(EnumType.STRING)
    private CommunityRequestStatus status;

    public ConnectionRequests()
    {
        this.status = CommunityRequestStatus.PENDING;
    }

    public ConnectionRequests(User requester, User receiver)
    {
        this.requester = requester;
        this.receiver = receiver;
        this.status = CommunityRequestStatus.PENDING;
    }

    public ConnectionRequests(User requester, User receiver, CommunityRequestStatus status)
    {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }


    public UUID getRequestId()
    {
        return m_requestId;
    }

    public void setRequestId(UUID requestId)
    {
        m_requestId = requestId;
    }

    public User getRequester()
    {
        return requester;
    }

    public void setRequester(User requester)
    {
        this.requester = requester;
    }

    public User getReceiver()
    {
        return receiver;
    }

    public void setReceiver(User receiver)
    {
        this.receiver = receiver;
    }

    public CommunityRequestStatus getStatus()
    {
        return status;
    }

    public void setStatus(CommunityRequestStatus status)
    {
        this.status = status;
    }
}
