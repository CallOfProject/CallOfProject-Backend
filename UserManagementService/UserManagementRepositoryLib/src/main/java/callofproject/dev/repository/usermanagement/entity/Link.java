package callofproject.dev.repository.usermanagement.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "link")
public class Link
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private long link_id;
    @Column(name = "link_title", nullable = false, length = 100)
    private String linkTitle;
    @Column(name = "link", length = 100)
    private String link;

    @ManyToMany(mappedBy = "linkList", fetch = FetchType.LAZY)
    private Set<UserProfile> userProfiles;

    public Link()
    {
    }

    public Set<UserProfile> getUserProfiles()
    {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles)
    {
        this.userProfiles = userProfiles;
    }

    public long getLink_id()
    {
        return link_id;
    }

    public void setLink_id(long link_id)
    {
        this.link_id = link_id;
    }

    public String getLinkTitle()
    {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle)
    {
        this.linkTitle = linkTitle;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
