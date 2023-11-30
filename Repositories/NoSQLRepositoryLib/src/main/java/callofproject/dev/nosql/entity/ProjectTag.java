/*----------------------------------------------------------------
	FILE		: ProjectTag.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	ProjectTag class represent the entity layer of the ProjectTag entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.entity;

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
    private String id;
    private String tagName;
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
    public String getId()
    {
        return id;
    }

    /**
     * Set tag project id
     *
     * @param id tag project id
     */
    public void setId(String id)
    {
        this.id = id;
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
