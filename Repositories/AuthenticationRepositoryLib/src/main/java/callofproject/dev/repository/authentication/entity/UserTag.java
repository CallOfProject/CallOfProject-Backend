package callofproject.dev.repository.authentication.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_tag")
public class UserTag
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tag_id")
    private UUID tagId;

    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;

    @ManyToMany(mappedBy = "tagList", fetch = FetchType.EAGER)
    private Set<UserProfile> userProfiles;

    public UserTag()
    {
    }


    public UserTag(String tagName, Set<UserProfile> userProfiles)
    {
        this.tagName = tagName;
        this.userProfiles = userProfiles;
    }

    public UserTag(String tagName)
    {
        this.tagName = tagName;
    }

    public UUID getTagId()
    {
        return tagId;
    }

    public void setTagId(UUID tagId)
    {
        this.tagId = tagId;
    }

    public String getTagName()
    {
        return tagName;
    }

    public void setTagName(String tagName)
    {
        this.tagName = tagName;
    }

    public Set<UserProfile> getUserProfiles()
    {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles)
    {
        this.userProfiles = userProfiles;
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof UserTag userTag && tagName.equals(userTag.tagName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tagId, tagName);
    }
}
