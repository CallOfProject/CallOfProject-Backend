package callofproject.dev.repository.authentication.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long m_roleId;
    @Column(name = "name")
    private String m_name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    public Role()
    {

    }

    public Role(String roleName)
    {
        m_name = roleName;
    }

    public long getRoleId()
    {
        return m_roleId;
    }

    public void setRoleId(long roleId)
    {
        m_roleId = roleId;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
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