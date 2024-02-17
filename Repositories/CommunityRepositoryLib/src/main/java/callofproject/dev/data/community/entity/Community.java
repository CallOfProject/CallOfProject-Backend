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

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "project_owner_id", nullable = false)
    private UUID projectOwnerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "community_status", nullable = false)
    private CommunityStatus communityStatus;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "community_users",
            joinColumns = @JoinColumn(name = "community_id", referencedColumnName = "community_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false))
    private Set<User> users;

    public Community()
    {
        communityStatus = CommunityStatus.OPEN;
    }

    public Community(String communityName, UUID projectId, UUID projectOwnerId)
    {
        this.communityName = communityName;
        this.projectId = projectId;
        this.projectOwnerId = projectOwnerId;
        communityStatus = CommunityStatus.OPEN;
    }

    public void addUser(User user)
    {
        if (users == null)
            users = new HashSet<>();

        this.users.add(user);
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

    public UUID getProjectId()
    {
        return projectId;
    }

    public void setProjectId(UUID projectId)
    {
        this.projectId = projectId;
    }

    public UUID getProjectOwnerId()
    {
        return projectOwnerId;
    }

    public void setProjectOwnerId(UUID projectOwnerId)
    {
        this.projectOwnerId = projectOwnerId;
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