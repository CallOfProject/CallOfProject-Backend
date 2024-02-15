package callofproject.dev.data.community.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @Column(name = "deleted_at")
    private LocalDateTime m_deletedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserConnection> connections = new HashSet<>();

    // Kullanıcı tarafından engellenen kullanıcılar
    @OneToMany(mappedBy = "blocker", fetch = FetchType.EAGER)
    private Set<BlockedConnections> blockedUsers;

    // Kullanıcıya gönderilen bağlantı istekleri
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private Set<ConnectionRequests> receivedConnectionRequests;

    // Kullanıcı tarafından gönderilen bağlantı istekleri
    @OneToMany(mappedBy = "requester", fetch = FetchType.EAGER)
    private Set<ConnectionRequests> sentConnectionRequests;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> roles;


    public User()
    {
        m_deletedAt = null;
    }

    public User(UUID userId, String username, String email, String firstName, String middleName, String lastName, Set<Role> roles)
    {
        this.roles = roles;
        m_userId = userId;
        m_username = username;
        m_email = email;
        m_firstName = firstName;
        m_middleName = middleName;
        m_lastName = lastName;
        m_deletedAt = null;
    }

    public void addConnection(UserConnection user)
    {
        if (connections == null)
            connections = new HashSet<>();

        connections.add(user);
    }

    public void removeConnection(UserConnection user)
    {
        connections.remove(user);
    }


    public void addConnectionRequestReceived(ConnectionRequests connectionRequest)
    {
        if (receivedConnectionRequests == null)
            receivedConnectionRequests = new HashSet<>();

        receivedConnectionRequests.add(connectionRequest);
    }

    public void removeConnectionRequestReceived(ConnectionRequests connectionRequest)
    {
        receivedConnectionRequests.remove(connectionRequest);
    }


    public void addConnectionRequestSent(ConnectionRequests connectionRequest)
    {
        if (sentConnectionRequests == null)
            sentConnectionRequests = new HashSet<>();

        sentConnectionRequests.add(connectionRequest);
    }

    public void removeConnectionRequestSent(ConnectionRequests connectionRequest)
    {
        sentConnectionRequests.remove(connectionRequest);
    }


    public void addBlockedUser(BlockedConnections user)
    {
        if (blockedUsers == null)
            blockedUsers = new HashSet<>();

        blockedUsers.add(user);
    }

    public void removeBlockedUser(BlockedConnections user)
    {
        blockedUsers.remove(user);
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

    public Set<UserConnection> getConnections()
    {
        return connections;
    }

    public void setConnections(Set<UserConnection> connections)
    {
        this.connections = connections;
    }

    public Set<BlockedConnections> getBlockedUsers()
    {
        return blockedUsers;
    }

    public void setBlockedUsers(Set<BlockedConnections> blockedUsers)
    {
        this.blockedUsers = blockedUsers;
    }

    public Set<ConnectionRequests> getReceivedConnectionRequests()
    {
        return receivedConnectionRequests;
    }

    public void setReceivedConnectionRequests(Set<ConnectionRequests> receivedConnectionRequests)
    {
        this.receivedConnectionRequests = receivedConnectionRequests;
    }

    public Set<ConnectionRequests> getSentConnectionRequests()
    {
        return sentConnectionRequests;
    }

    public void setSentConnectionRequests(Set<ConnectionRequests> sentConnectionRequests)
    {
        this.sentConnectionRequests = sentConnectionRequests;
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