package callofproject.dev.data.project.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "project_participant_request")
public class ProjectParticipantRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_participant_request_id")
    private UUID m_participantRequestId;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "project_id", nullable = false)
    private Project m_project;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private User m_user;
    @Column(name = "is_accepted", nullable = false)
    private boolean m_isAccepted = false;
    @Column(name = "request_date", nullable = false)
    private LocalDateTime m_requestDate;

    public ProjectParticipantRequest()
    {
        m_requestDate = LocalDateTime.now();
    }


    public ProjectParticipantRequest(Project project, User user)
    {
        m_project = project;
        m_user = user;
        m_requestDate = LocalDateTime.now();
    }


    public boolean isAccepted()
    {
        return m_isAccepted;
    }

    public void setAccepted(boolean accepted)
    {
        m_isAccepted = accepted;
    }

    public LocalDateTime getRequestDate()
    {
        return m_requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate)
    {
        m_requestDate = requestDate;
    }

    public UUID getParticipantRequestId()
    {
        return m_participantRequestId;
    }

    public void setParticipantRequestId(UUID participantRequestId)
    {
        m_participantRequestId = participantRequestId;
    }

    public Project getProject()
    {
        return m_project;
    }

    public void setProject(Project project)
    {
        m_project = project;
    }

    public User getUser()
    {
        return m_user;
    }

    public void setUser(User user)
    {
        m_user = user;
    }
}
