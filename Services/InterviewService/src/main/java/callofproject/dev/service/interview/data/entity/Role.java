package callofproject.dev.service.interview.data.entity;

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
        return this.user;
    }

    public void setUser(Set<User> user)
    {
        this.user = user;
    }

    public Role(String roleName)
    {
        this.m_name = roleName;
    }

    @JsonIgnore
    public long getRoleId()
    {
        return this.m_roleId;
    }

    public void setRoleId(long roleId)
    {
        this.m_roleId = roleId;
    }

    public String getName()
    {
        return this.m_name;
    }

    public void setName(String name)
    {
        this.m_name = name;
    }

    @JsonIgnore
    public String getAuthority()
    {
        return this.m_name;
    }

    public void addUser(User userForRole)
    {
        boolean isExistsUser = user.stream().anyMatch(usr -> usr.getUsername().equals(userForRole.getUsername()));

        if (!isExistsUser)
            user.add(userForRole);
    }
}