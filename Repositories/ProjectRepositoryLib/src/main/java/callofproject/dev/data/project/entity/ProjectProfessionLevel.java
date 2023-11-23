package callofproject.dev.data.project.entity;

import callofproject.dev.data.project.entity.enums.EProjectProfessionLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "project_profession_level")
public class ProjectProfessionLevel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_profession_level_id")
    private long m_projectProfessionLevelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_profession_level_name", unique = true)
    private EProjectProfessionLevel m_projectProfessionLevel;

    public ProjectProfessionLevel()
    {
    }

    public ProjectProfessionLevel(EProjectProfessionLevel projectProfessionLevel)
    {
        m_projectProfessionLevel = projectProfessionLevel;
    }

    public long getProjectProfessionLevelId()
    {
        return m_projectProfessionLevelId;
    }

    public void setProjectProfessionLevelId(long projectProfessionLevelId)
    {
        m_projectProfessionLevelId = projectProfessionLevelId;
    }

    public EProjectProfessionLevel getProjectProfessionLevel()
    {
        return m_projectProfessionLevel;
    }

    public void setProjectProfessionLevel(EProjectProfessionLevel projectProfessionLevel)
    {
        m_projectProfessionLevel = projectProfessionLevel;
    }
}
