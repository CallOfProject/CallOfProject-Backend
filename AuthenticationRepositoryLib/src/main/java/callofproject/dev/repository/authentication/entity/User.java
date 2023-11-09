package callofproject.dev.repository.authentication.entity;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cop_user")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name", length = 80, nullable = false)
    private String firstName;

    @Column(name = "middle_name", length = 50, nullable = false)
    private String middleName;
    @Column(name = "last_name", length = 80, nullable = false)
    private String lastName;

    @Column(name = "username", length = 80, nullable = false, unique = true)
    private String username;
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private UserProfile userProfile;

    @Column(name = "is_account_expired")
    private boolean isAccountExpired = true;

    @Column(name = "is_account_blocked")
    private boolean isAccountBlocked = false;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    private Set<Role> roles;


    public User()
    {
        if (roles == null)
            roles = new HashSet<>();

        roles.add(new Role("ROLE_USER"));
    }

    public User(String username, String firstName, String middleName, String lastName,
                String email, String password, LocalDate birthDate, Role role)
    {
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;

        if (roles == null)
            roles = new HashSet<>();

        roles.add(role);
    }

    public User(String username, String firstName, String lastName, String email, String password, LocalDate birthDate)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        middleName = "";

        if (roles == null)
            roles = new HashSet<>();

        roles.add(new Role("ROLE_USER"));
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return !isAccountBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setAccountExpired(boolean accountExpired)
    {
        isAccountExpired = accountExpired;
    }

    public void setAccountLocked(boolean accountLocked)
    {
        isAccountBlocked = accountLocked;
    }

    public void setEnabled(boolean enabled)
    {
        isEnabled = enabled;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }

    public LocalDate getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate)
    {
        this.creationDate = creationDate;
    }

    public UserProfile getUserProfile()
    {
        return userProfile;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public void setUserProfile(UserProfile userProfile)
    {
        this.userProfile = userProfile;
    }

}
