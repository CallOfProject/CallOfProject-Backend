package callofproject.dev.data.community.entity;

import callofproject.dev.data.community.entity.enumeration.CommunityStatus;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "community")
public class Community
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "community_id")
    private UUID communityId;

    @Column(name = "community_name", nullable = false)
    private String communityName;


    @Enumerated(EnumType.STRING)
    @Column(name = "community_status", nullable = false)
    private CommunityStatus communityStatus;

    @OneToOne(mappedBy = "m_community",fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Project project;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "community_users",
            joinColumns = @JoinColumn(name = "community_id", referencedColumnName = "community_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false))
    private Set<User> users;

    public Community()
    {
        communityStatus = CommunityStatus.OPEN;
    }

    public Community(String communityName)
    {
        this.communityName = communityName;
        communityStatus = CommunityStatus.OPEN;
    }

    public void addUser(User user)
    {
        if (users == null)
            users = new HashSet<>();

        this.users.add(user);
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public CommunityStatus getCommunityStatus()
    {
        return communityStatus;
    }

    public void setCommunityStatus(CommunityStatus communityStatus)
    {
        this.communityStatus = communityStatus;
    }


    public UUID getCommunityId()
    {
        return communityId;
    }

    public void setCommunityId(UUID communityId)
    {
        this.communityId = communityId;
    }

    public String getCommunityName()
    {
        return communityName;
    }

    public void setCommunityName(String communityName)
    {
        this.communityName = communityName;
    }


    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }
}