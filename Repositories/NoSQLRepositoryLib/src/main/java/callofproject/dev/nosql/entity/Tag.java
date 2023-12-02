package callofproject.dev.nosql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("tag")
public class Tag
{
    @Id
    private String id;

    @Column(unique = true)
    private String tagName;

    public Tag()
    {
    }

    public Tag(String tagName)
    {
        this.tagName = tagName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }
}
