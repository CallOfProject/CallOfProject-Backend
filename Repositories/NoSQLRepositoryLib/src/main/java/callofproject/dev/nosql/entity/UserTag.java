/*----------------------------------------------------------------
	FILE		: UserTag.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	UserTag class represent the entity layer of the UserTag entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.entity;


import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * UserTag class represent the entity layer of the UserTag entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Document("user_tag")
@SuppressWarnings("all")
public class UserTag
{
    @Id
    private String id;
    private String tagName;
    private UUID userId;

    /**
     * Default Constructor
     */
    public UserTag()
    {

    }

    /**
     * Constructor
     *
     * @param tagName tag name
     * @param userId  user id
     */
    public UserTag(String tagName, UUID userId)
    {
        this.tagName = tagName;
        this.userId = userId;
    }

    /**
     * Get tag user id
     *
     * @return tag user id
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set tag user id
     *
     * @param id tag user id
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
     * Get user id
     *
     * @return user id
     */
    public UUID getUserId()
    {
        return userId;
    }

    /**
     * Set user id
     *
     * @param userId user id
     */
    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }
}
