package callofproject.dev.data.project.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_projects")
public class UserProjects
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_project_id")
    private long m_userProjectId;

    @Column(name = "user_id")
    private UUID m_userId;
    @Column(name = "project_id")
    private UUID m_projectId;

    public UserProjects()
    {
    }

    public UserProjects(UUID userId, UUID projectId)
    {
        m_userId = userId;
        m_projectId = projectId;
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

    public long getUserProjectId()
    {
        return m_userProjectId;
    }

    public void setUserProjectId(long userProjectId)
    {
        m_userProjectId = userProjectId;
    }
}
