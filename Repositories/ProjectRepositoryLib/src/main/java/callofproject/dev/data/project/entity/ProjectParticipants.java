package callofproject.dev.data.project.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "project_participants")
public class ProjectParticipants
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_participants_id")
    private long m_projectParticipantsId;

    @Column(name = "user_id")
    private UUID m_userId;

    @Column(name = "project_id")
    private UUID m_projectId;

    public ProjectParticipants()
    {

    }

    public ProjectParticipants(UUID userId, UUID projectId)
    {
        m_userId = userId;
        m_projectId = projectId;
    }

    public long getProjectParticipantsId()
    {
        return m_projectParticipantsId;
    }

    public void setProjectParticipantsId(long projectParticipantsId)
    {
        m_projectParticipantsId = projectParticipantsId;
    }

    public UUID getUserId()
    {
        return m_userId;
    }

    public void setUserId(UUID userId)
    {
        m_userId = userId;
    }

    public UUID getProjectId()
    {
        return m_projectId;
    }

    public void setProjectId(UUID projectId)
    {
        m_projectId = projectId;
    }
}
