package callofproject.dev.data.task.entity;

import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "task")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "task_id")
    private UUID m_taskId;

    @Column(name = "title")
    private String m_title;

    @Column(name = "description")
    private String m_description;

    @ManyToMany(mappedBy = "m_assignedTasks", fetch = FetchType.EAGER, cascade = {PERSIST, REFRESH, MERGE})
    @JsonIgnore
    private Set<User> m_assignees;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority m_priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus m_taskStatus;

    @Column(name = "start_date")
    private LocalDate m_startDate;

    @Column(name = "end_date")
    private LocalDate m_endDate;

    @ManyToOne(cascade = {ALL})
    @JoinColumn(name = "project_id", nullable = false)
    private Project m_project;


    public Task()
    {
        m_assignees = new HashSet<>();
    }

    public Task(Project project, String title, String description, Priority priority, LocalDate startDate, LocalDate endDate)
    {
        m_project = project;
        m_title = title;
        m_description = description;
        m_priority = priority;
        m_startDate = startDate;
        m_endDate = endDate;
        m_taskStatus = TaskStatus.NOT_STARTED;
        m_assignees = new HashSet<>();
    }

    public void addAssignee(User user)
    {
        if (m_assignees == null)
            m_assignees = new HashSet<>();

        m_assignees.add(user);
    }

    public Project getProject()
    {
        return m_project;
    }

    public void setProject(Project project)
    {
        m_project = project;
    }

    public UUID getTaskId()
    {
        return m_taskId;
    }

    public void setTaskId(UUID taskId)
    {
        m_taskId = taskId;
    }

    public String getTitle()
    {
        return m_title;
    }

    public void setTitle(String title)
    {
        m_title = title;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public Set<User> getAssignees()
    {
        return m_assignees;
    }

    public void setAssignees(Set<User> assignees)
    {
        m_assignees = assignees;
    }

    public Priority getPriority()
    {
        return m_priority;
    }

    public void setPriority(Priority priority)
    {
        m_priority = priority;
    }

    public TaskStatus getTaskStatus()
    {
        return m_taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus)
    {
        m_taskStatus = taskStatus;
    }

    public LocalDate getStartDate()
    {
        return m_startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        m_startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        return m_endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        m_endDate = endDate;
    }
}
