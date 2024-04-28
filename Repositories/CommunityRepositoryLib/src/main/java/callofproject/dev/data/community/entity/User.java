package callofproject.dev.data.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @Column(name = "user_id")
    private UUID m_userId;

    @Column(name = "username", nullable = false)
    private String m_username;

    @Column(name = "email", nullable = false)
    private String m_email;

    @Column(name = "first_name", nullable = false)
    private String m_firstName;

    @Column(name = "middle_name", nullable = false)
    private String m_middleName;

    @Column(name = "last_name", nullable = false)
    private String m_lastName;

    @Column(name = "profile_photo")
    private String m_profilePhoto;

    @Column(name = "deleted_at")
    private LocalDateTime m_deletedAt;

/*    @OneToMany(fetch = FetchType.EAGER, cascade = {PERSIST, REFRESH})
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "main_user_id"),
            inverseJoinColumns = @JoinColumn(name = "connected_user_id")
    )
    private Set<User> connections = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {PERSIST, REFRESH})
    @JoinTable(
            name = "connection_requests",
            joinColumns = @JoinColumn(name = "main_user_id"),
            inverseJoinColumns = @JoinColumn(name = "requester_user_id")
    )
    private Set<User> connectionRequests = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {PERSIST, REFRESH})
    @JoinTable(
            name = "blocked_connections",
            joinColumns = @JoinColumn(name = "main_user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_user_id")
    )
    private Set<User> blockedConnections = new HashSet<>();*/


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> roles;


    @OneToMany(mappedBy = "m_user", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ProjectParticipant> m_projectParticipants = new HashSet<>(); // projects that user is participant

    @OneToMany(mappedBy = "m_requester", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    private Set<ConnectionRequest> connectionRequests = new HashSet<>();

    @OneToMany(mappedBy = "m_mainUser", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    private Set<UserConnection> connections = new HashSet<>();

    @OneToMany(mappedBy = "m_mainUser", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    private Set<BlockConnection> blockedConnections = new HashSet<>();

    public User()
    {
        m_deletedAt = null;
    }

    public User(UUID userId, String username, String email, String firstName, String middleName, String lastName, Set<Role> roles,
                String profilePhoto)
    {
        this.roles = roles;
        m_userId = userId;
        m_username = username;
        m_email = email;
        m_firstName = firstName;
        m_middleName = middleName;
        m_lastName = lastName;
        m_profilePhoto = profilePhoto;
        m_deletedAt = null;
    }


    public void addUserConnection(UserConnection userConnection)
    {
        connections.add(userConnection);
    }

    public void addConnectionRequest(ConnectionRequest connectionRequest)
    {
        connectionRequests.add(connectionRequest);
    }

    public void addBlockedConnection(BlockConnection blockConnection)
    {
        blockedConnections.add(blockConnection);
    }

    public void removeUserConnection(UserConnection userConnection)
    {
        connections.remove(userConnection);
    }
    public String getProfilePhoto()
    {
        return m_profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto)
    {
        m_profilePhoto = profilePhoto;
    }

    public Set<ProjectParticipant> getProjectParticipants()
    {
        return m_projectParticipants;
    }

    public void setProjectParticipants(Set<ProjectParticipant> projectParticipants)
    {
        m_projectParticipants = projectParticipants;
    }

    public Set<ConnectionRequest> getConnectionRequests()
    {
        return connectionRequests;
    }

    public void setConnectionRequests(Set<ConnectionRequest> connectionRequests)
    {
        this.connectionRequests = connectionRequests;
    }

    public Set<UserConnection> getConnections()
    {
        return connections;
    }

    public void setConnections(Set<UserConnection> connections)
    {
        this.connections = connections;
    }

    public Set<BlockConnection> getBlockedConnections()
    {
        return blockedConnections;
    }

    public void setBlockedConnections(Set<BlockConnection> blockedConnections)
    {
        this.blockedConnections = blockedConnections;
    }

    public UUID getUserId()
    {
        return m_userId;
    }

    public void setUserId(UUID userId)
    {
        m_userId = userId;
    }

    public String getUsername()
    {
        return m_username;
    }

    public void setUsername(String username)
    {
        m_username = username;
    }

    public String getEmail()
    {
        return m_email;
    }

    public void setEmail(String email)
    {
        m_email = email;
    }

    public String getFirstName()
    {
        return m_firstName;
    }

    public void setFirstName(String firstName)
    {
        m_firstName = firstName;
    }

    public String getMiddleName()
    {
        return m_middleName;
    }

    public void setMiddleName(String middleName)
    {
        m_middleName = middleName;
    }

    public String getLastName()
    {
        return m_lastName;
    }

    public void setLastName(String lastName)
    {
        m_lastName = lastName;
    }

    public LocalDateTime getDeletedAt()
    {
        return m_deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt)
    {
        m_deletedAt = deletedAt;
    }


    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public String getFullName()
    {
        if (m_middleName == null || m_middleName.isBlank() || m_middleName.isEmpty())
            return String.format("%s %s", m_firstName, m_lastName);

        return String.format("%s %s %s", m_firstName, m_middleName, m_lastName);
    }
}