package callofproject.dev.repository.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long m_roleId;
    @Column(name = "name")
    private String m_name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> user;

    public Role()
    {

    }

    public Set<User> getUser()
    {
        return user;
    }

    public void setUser(Set<User> user)
    {
        this.user = user;
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


    @Override
    public String getAuthority()
    {
        return m_name;
    }

    public void addUser(User userForRole)
    {
        var isExistsUser = user.stream().anyMatch(usr -> usr.getUsername().equals(userForRole.getUsername()));

        if (!isExistsUser)
            user.add(userForRole);
    }
}