package callofproject.dev.data.task.entity;


import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.data.task.entity.enums.EProjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "project")
@SuppressWarnings("all")
public class Project
{
    @Id
    @Column(name = "project_id")
    private UUID m_projectId;

    @Column(name = "title", nullable = false, length = 100)
    private String m_projectName;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User m_projectOwner;

    @OneToMany(mappedBy = "m_project", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ProjectParticipant> m_projectParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private EProjectStatus m_projectStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_operation_status")
    private AdminOperationStatus m_adminOperationStatus;

    @OneToMany(mappedBy = "m_project", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Task> m_tasks;

    public Project()
    {
    }

    public Project(UUID projectId, String projectName, User projectOwner)
    {
        m_projectId = projectId;
        m_projectName = projectName;
        m_projectOwner = projectOwner;
    }

    public Project(UUID projectId, String projectName, User projectOwner, Set<ProjectParticipant> projectParticipants,
                   EProjectStatus projectStatus, AdminOperationStatus adminOperationStatus)
    {
        m_projectId = projectId;
        m_projectName = projectName;
        m_projectOwner = projectOwner;
        m_projectParticipants = projectParticipants;
        m_projectStatus = projectStatus;
        m_adminOperationStatus = adminOperationStatus;
    }

    public void addTask(Task task)
    {
        if (m_tasks == null)
            m_tasks = new HashSet<>();

        m_tasks.add(task);
    }

    public Set<Task> getTasks()
    {
        return m_tasks;
    }

    public void setTasks(Set<Task> tasks)
    {
        m_tasks = tasks;
    }

    public UUID getProjectId()
    {
        return m_projectId;
    }

    public void setProjectId(UUID projectId)
    {
        m_projectId = projectId;
    }

    public String getProjectName()
    {
        return m_projectName;
    }

    public void setProjectName(String projectName)
    {
        m_projectName = projectName;
    }

    public User getProjectOwner()
    {
        return m_projectOwner;
    }

    public void setProjectOwner(User projectOwner)
    {
        m_projectOwner = projectOwner;
    }

    public Set<ProjectParticipant> getProjectParticipants()
    {
        return m_projectParticipants;
    }

    public void setProjectParticipants(Set<ProjectParticipant> projectParticipants)
    {
        m_projectParticipants = projectParticipants;
    }

    public EProjectStatus getProjectStatus()
    {
        return m_projectStatus;
    }

    public void setProjectStatus(EProjectStatus projectStatus)
    {
        m_projectStatus = projectStatus;
    }

    public AdminOperationStatus getAdminOperationStatus()
    {
        return m_adminOperationStatus;
    }

    public void setAdminOperationStatus(AdminOperationStatus adminOperationStatus)
    {
        m_adminOperationStatus = adminOperationStatus;
    }
}