package callofproject.dev.repository.repository.project.entity.nosql;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("project_tag")
@SuppressWarnings("all")
public class ProjectTag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_project_id")
    private long m_tagProjectId;

    @Column(name = "tag_name")
    private String tagName;
    @Column(name = "project_id")
    private UUID projectId;

    public ProjectTag(String tagName, UUID projectId)
    {
        this.tagName = tagName;
        this.projectId = projectId;
    }

    public long getTagProjectId()
    {
        return m_tagProjectId;
    }

    public void setTagProjectId(long tagProjectId)
    {
        m_tagProjectId = tagProjectId;
    }

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    public UUID getProjectId()
    {
        return projectId;
    }

    public void setProjectId(UUID projectId)
    {
        this.projectId = projectId;
    }
}
