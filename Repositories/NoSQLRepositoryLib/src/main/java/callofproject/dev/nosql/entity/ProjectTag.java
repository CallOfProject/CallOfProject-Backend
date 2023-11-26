/*----------------------------------------------------------------
	FILE		: ProjectTag.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	ProjectTag class represent the entity layer of the ProjectTag entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * ProjectTag class represent the entity layer of the ProjectTag entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
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

    /**
     * Constructor
     */
    public ProjectTag()
    {
    }

    /**
     * Constructor
     *
     * @param tagName   tag name
     * @param projectId project id
     */
    public ProjectTag(String tagName, UUID projectId)
    {
        this.tagName = tagName;
        this.projectId = projectId;
    }

    /**
     * Get tag project id
     *
     * @return tag project id
     */
    public long getTagProjectId()
    {
        return m_tagProjectId;
    }

    /**
     * Set tag project id
     *
     * @param tagProjectId tag project id
     */
    public void setTagProjectId(long tagProjectId)
    {
        m_tagProjectId = tagProjectId;
    }

    /**
     * Get tag name
     *
     * @return tag name
     */
    public String getTagName()
    {
        return tagName;
    }

    /**
     * Set tag name
     *
     * @param tagName tag name
     */
    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    /**
     * Get project id
     *
     * @return project id
     */
    public UUID getProjectId()
    {
        return projectId;
    }

    /**
     * Set project id
     *
     * @param projectId project id
     */
    public void setProjectId(UUID projectId)
    {
        this.projectId = projectId;
    }
}
