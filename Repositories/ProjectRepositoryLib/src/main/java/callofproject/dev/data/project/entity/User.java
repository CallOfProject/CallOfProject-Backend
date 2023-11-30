package callofproject.dev.data.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user")
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

    @OneToMany(mappedBy = "m_projectOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Project> m_projects; // projects that he owns

    @OneToMany(mappedBy = "m_user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProjectParticipant> m_projectParticipants; // projects that he owns

    @OneToMany(mappedBy = "m_user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProjectParticipantRequest> m_projectParticipantRequests; // Project Join requests


    public User()
    {
    }

    public User(UUID userId, String username, String email, String firstName, String middleName, String lastName)
    {
        m_userId = userId;
        m_username = username;
        m_email = email;
        m_firstName = firstName;
        m_middleName = middleName;
        m_lastName = lastName;
    }


    public void addOwnProject(Project project)
    {
        if (m_projects == null)
            m_projects = new HashSet<>();

        m_projects.add(project);
    }

    public Set<ProjectParticipantRequest> getProjectParticipantRequests()
    {
        return m_projectParticipantRequests;
    }

    public void setProjectParticipantRequests(Set<ProjectParticipantRequest> projectParticipantRequests)
    {
        m_projectParticipantRequests = projectParticipantRequests;
    }

    public Set<ProjectParticipant> getProjectParticipants()
    {
        return m_projectParticipants;
    }

    public void setProjectParticipants(Set<ProjectParticipant> projectParticipants)
    {
        m_projectParticipants = projectParticipants;
    }

    public Set<Project> getProjects()
    {
        return m_projects;
    }

    public void setProjects(Set<Project> projects)
    {
        m_projects = projects;
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

    public String getFullName()
    {
        if (m_middleName == null || m_middleName.isBlank() || m_middleName.isEmpty())
            return String.format("%s %s", m_firstName, m_lastName);

        return String.format("%s %s %s", m_firstName, m_middleName, m_lastName);
    }
}
