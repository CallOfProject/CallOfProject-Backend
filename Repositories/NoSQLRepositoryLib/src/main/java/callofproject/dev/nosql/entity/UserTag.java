/*----------------------------------------------------------------
	FILE		: UserTag.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	UserTag class represent the entity layer of the UserTag entity.
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

@Document("user_tag")
@SuppressWarnings("all")
public class UserTag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_user_id")
    private long m_tagUserId;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "user_id")
    private UUID userId;


    public UserTag(String tagName, UUID userId)
    {
        this.tagName = tagName;
        this.userId = userId;
    }

    public long getTagUserId()
    {
        return m_tagUserId;
    }

    public void setTagUserId(long tagUserId)
    {
        m_tagUserId = tagUserId;
    }

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }
}
