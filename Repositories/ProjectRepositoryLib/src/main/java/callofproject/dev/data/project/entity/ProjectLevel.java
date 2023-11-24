package callofproject.dev.data.project.entity;

import callofproject.dev.data.project.entity.enums.EProjectLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "project_level")
public class ProjectLevel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_level_id")
    @JsonIgnore
    private long m_projectLevelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_level_name", unique = true)
    private EProjectLevel m_projectLevel;

    public ProjectLevel(EProjectLevel projectLevel)
    {
        m_projectLevel = projectLevel;
    }

    public ProjectLevel()
    {
    }

    public long getProjectLevelId()
    {
        return m_projectLevelId;
    }

    public void setProjectLevelId(long projectLevelId)
    {
        m_projectLevelId = projectLevelId;
    }

    public EProjectLevel getProjectLevel()
    {
        return m_projectLevel;
    }

    public void setProjectLevel(EProjectLevel projectLevel)
    {
        m_projectLevel = projectLevel;
    }
}
