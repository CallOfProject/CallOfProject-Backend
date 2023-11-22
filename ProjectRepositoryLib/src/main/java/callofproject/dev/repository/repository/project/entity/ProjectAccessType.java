package callofproject.dev.repository.repository.project.entity;

import callofproject.dev.repository.repository.project.entity.enums.EProjectAccessType;
import jakarta.persistence.*;

@Entity
@Table(name = "project_access_type")
public class ProjectAccessType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_access_type_id")
    private long m_projectAccessTypeId;
    @Enumerated(EnumType.STRING)
    @Column(name = "project_access_type_name", unique = true)
    private EProjectAccessType m_projectAccessType;

    public ProjectAccessType()
    {
    }

    public ProjectAccessType(EProjectAccessType projectAccessType)
    {
        m_projectAccessType = projectAccessType;
    }

    public long getProjectAccessTypeId()
    {
        return m_projectAccessTypeId;
    }

    public void setProjectAccessTypeId(long projectAccessTypeId)
    {
        m_projectAccessTypeId = projectAccessTypeId;
    }

    public EProjectAccessType getProjectAccessType()
    {
        return m_projectAccessType;
    }

    public void setProjectAccessType(EProjectAccessType projectAccessType)
    {
        m_projectAccessType = projectAccessType;
    }
}
