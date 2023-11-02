package callofproject.dev.repository.usermanagement.entity;

import jakarta.persistence.*;

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
}