package callofproject.dev.data.project.entity;

import callofproject.dev.data.project.entity.enums.EFeedbackTimeRange;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.library.exception.repository.RepositoryException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static java.time.LocalDate.now;

@Entity
@Table(name = "project")
@SuppressWarnings("all")
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id")
    private UUID m_projectId;
    @Column(name = "project_image", nullable = true)
    private String m_projectImagePath;
    @Column(name = "title", nullable = false, length = 100)
    private String m_projectName;
    @Column(name = "summary", nullable = false, length = 200)
    private String m_projectSummary;
    @Column(name = "description", nullable = false, length = 500)
    private String m_description;
    @Column(name = "aim", nullable = false, length = 250)
    private String m_projectAim;
    @Column(name = "application_deadline", nullable = false)
    private LocalDate m_applicationDeadline; // Last date for application
    @Column(name = "expected_completion_date", nullable = false)
    private LocalDate m_expectedCompletionDate; // Expected completion date
    @Column(name = "start_date", nullable = false)
    private LocalDate m_startDate; // Project start date
    @Column(name = "completion_date", nullable = true)
    private LocalDate m_completionDate; // Project completion date
    @Column(name = "max_participant")
    private int m_maxParticipant;
    @Column(name = "invite_link")
    private String m_inviteLink;
    @Column(name = "technical_requirements", length = 200)
    private String m_technicalRequirements;
    @Column(name = "special_requirements", length = 200)
    private String m_specialRequirements;
    @ManyToOne
    @JoinColumn(name = "project_access_type_id", nullable = false)
    private ProjectAccessType m_projectAccessType;
    @ManyToOne
    @JoinColumn(name = "project_profession_level_id", nullable = false)
    private ProjectProfessionLevel m_professionLevel;
    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector m_sector;
    @ManyToOne
    @JoinColumn(name = "degree_id", nullable = false)
    private Degree m_degree;
    @ManyToOne
    @JoinColumn(name = "project_level_id", nullable = false)
    private ProjectLevel m_projectLevel;
    @ManyToOne
    @JoinColumn(name = "interview_type_id", nullable = false)
    private InterviewType m_interviewType;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User m_projectOwner;

    @OneToMany(mappedBy = "m_project", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ProjectParticipant> m_projectParticipants; // Approval requests

    @OneToMany(mappedBy = "m_project", cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProjectParticipantRequest> m_projectParticipantRequests;

    @Column(name = "admin_note", length = 200)
    private String m_adminNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private EProjectStatus m_projectStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_time_range")
    private EFeedbackTimeRange m_feedbackTimeRange;


    public Project()
    {
    }


    public String getAdminNote()
    {
        return m_adminNote;
    }

    public void setProjectOwner(User projectOwner)
    {
        m_projectOwner = projectOwner;
    }

    public void setAdminNote(String adminNote)
    {
        m_adminNote = adminNote;
    }

    public void finishProject()
    {
        m_projectStatus = EProjectStatus.FINISHED;
        m_completionDate = now();
    }
    public void addProjectParticipant(User user)
    {
        if (m_projectParticipants == null)
            m_projectParticipants = new HashSet<>();

        m_projectParticipants.add(new ProjectParticipant(this, user));
    }

    public void addProjectParticipant(ProjectParticipant projectParticipant)
    {
        if (m_projectParticipants == null)
            m_projectParticipants = new HashSet<>();

        m_projectParticipants.add(projectParticipant);
    }

    public void addProjectParticipantRequest(User user)
    {
        if (m_projectParticipantRequests == null)
            m_projectParticipantRequests = new HashSet<>();

        if (m_maxParticipant <= m_projectParticipantRequests.size())
            throw new RepositoryException("Project is full! You cannot join this project");

        m_projectParticipantRequests.add(new ProjectParticipantRequest(this, user));
    }

    public void addProjectParticipantRequest(ProjectParticipantRequest projectParticipantRequest)
    {
        if (m_projectParticipantRequests == null)
            m_projectParticipantRequests = new HashSet<>();

        if (m_maxParticipant <= m_projectParticipantRequests.size())
            throw new RepositoryException("Project is full! You cannot join this project");

        m_projectParticipantRequests.add(projectParticipantRequest);
    }


    public Project update(Project other)
    {
        m_projectName = other.getProjectName();
        m_projectSummary = other.getProjectSummary();
        m_description = other.getDescription();
        m_projectAim = other.getProjectAim();
        m_applicationDeadline = other.getApplicationDeadline();
        m_expectedCompletionDate = other.getExpectedCompletionDate();
        m_startDate = other.getStartDate();
        m_maxParticipant = other.getMaxParticipant();
        m_projectAccessType = other.getProjectAccessType();
        m_professionLevel = other.getProfessionLevel();
        m_sector = other.getSector();
        m_degree = other.getDegree();
        m_projectLevel = other.getProjectLevel();
        m_interviewType = other.getInterviewType();
        m_technicalRequirements = other.getTechnicalRequirements();
        m_specialRequirements = other.getSpecialRequirements();
        return this;
    }

    public static class Builder
    {
        private final Project m_project;

        public Builder()
        {
            m_project = new Project();
        }

        public Builder setProjectId(UUID projectId)
        {
            m_project.m_projectId = projectId;
            return this;
        }

        public Builder setProjectOwner(User projectOwner)
        {
            m_project.m_projectOwner = projectOwner;
            return this;
        }

        public Builder setFeedbackTimeRange(EFeedbackTimeRange feedbackTimeRange)
        {
            m_project.m_feedbackTimeRange = feedbackTimeRange;
            return this;
        }

        public Builder setProjectImagePath(String projectImagePath)
        {
            m_project.m_projectImagePath = projectImagePath;
            return this;
        }

        public Builder setProjectName(String projectName)
        {
            m_project.m_projectName = projectName;
            return this;
        }

        public Builder setProjectSummary(String projectSummary)
        {
            m_project.m_projectSummary = projectSummary;
            return this;
        }

        public Builder setDescription(String description)
        {
            m_project.m_description = description;
            return this;
        }

        public Builder setProjectAim(String projectAim)
        {
            m_project.m_projectAim = projectAim;
            return this;
        }

        public Builder setApplicationDeadline(LocalDate applicationDeadline)
        {
            m_project.m_applicationDeadline = applicationDeadline;
            return this;
        }

        public Builder setExpectedCompletionDate(LocalDate expectedCompletionDate)
        {
            m_project.m_expectedCompletionDate = expectedCompletionDate;
            return this;
        }

        public Builder setStartDate(LocalDate startDate)
        {
            m_project.m_startDate = startDate;
            return this;
        }

        public Builder setMaxParticipant(int maxParticipant)
        {
            m_project.m_maxParticipant = maxParticipant;
            return this;
        }

        public Builder setProjectAccessType(ProjectAccessType projectAccessType)
        {
            m_project.m_projectAccessType = projectAccessType;
            return this;
        }

        public Builder setProfessionLevel(ProjectProfessionLevel professionLevel)
        {
            m_project.m_professionLevel = professionLevel;
            return this;
        }

        public Builder setSector(Sector sector)
        {
            m_project.m_sector = sector;
            return this;
        }

        public Builder setDegree(Degree degree)
        {
            m_project.m_degree = degree;
            return this;
        }

        public Builder setProjectLevel(ProjectLevel projectLevel)
        {
            m_project.m_projectLevel = projectLevel;
            return this;
        }

        public Builder setInterviewType(InterviewType interviewType)
        {
            m_project.m_interviewType = interviewType;
            return this;
        }

        public Builder setInviteLink(String inviteLink)
        {
            m_project.m_inviteLink = inviteLink;
            return this;
        }

        public Builder setTechnicalRequirements(String technicalRequirements)
        {
            m_project.m_technicalRequirements = technicalRequirements;
            return this;
        }


        public Builder setSpecialRequirements(String specialRequirements)
        {
            m_project.m_specialRequirements = specialRequirements;
            return this;
        }

        public Project build()
        {
            m_project.m_projectStatus = m_project.m_startDate.isEqual(now()) ? EProjectStatus.IN_PROGRESS : EProjectStatus.NOT_STARTED;
            return m_project;
        }
    }



    public UUID getProjectId()
    {
        return m_projectId;
    }

    public String getProjectImagePath()
    {
        return m_projectImagePath;
    }

    public String getProjectName()
    {
        return m_projectName;
    }

    public String getProjectSummary()
    {
        return m_projectSummary;
    }

    public String getDescription()
    {
        return m_description;
    }

    public String getProjectAim()
    {
        return m_projectAim;
    }

    public LocalDate getApplicationDeadline()
    {
        return m_applicationDeadline;
    }

    public LocalDate getExpectedCompletionDate()
    {
        return m_expectedCompletionDate;
    }

    public LocalDate getStartDate()
    {
        return m_startDate;
    }

    public LocalDate getCompletionDate()
    {
        return m_completionDate;
    }

    public int getMaxParticipant()
    {
        return m_maxParticipant;
    }

    public String getInviteLink()
    {
        return m_inviteLink;
    }

    public String getTechnicalRequirements()
    {
        return m_technicalRequirements;
    }

    public String getSpecialRequirements()
    {
        return m_specialRequirements;
    }

    public ProjectAccessType getProjectAccessType()
    {
        return m_projectAccessType;
    }

    public ProjectProfessionLevel getProfessionLevel()
    {
        return m_professionLevel;
    }

    public Sector getSector()
    {
        return m_sector;
    }

    public Degree getDegree()
    {
        return m_degree;
    }

    public ProjectLevel getProjectLevel()
    {
        return m_projectLevel;
    }

    public InterviewType getInterviewType()
    {
        return m_interviewType;
    }

    public User getProjectOwner()
    {
        return m_projectOwner;
    }

    public Set<ProjectParticipant> getProjectParticipants()
    {
        return m_projectParticipants;
    }

    public Set<ProjectParticipantRequest> getProjectParticipantRequests()
    {
        return m_projectParticipantRequests;
    }

    public EProjectStatus getProjectStatus()
    {
        return m_projectStatus;
    }

    public EFeedbackTimeRange getFeedbackTimeRange()
    {
        return m_feedbackTimeRange;
    }

    public void setProjectId(UUID projectId)
    {
        m_projectId = projectId;
    }

    public void setProjectImagePath(String projectImagePath)
    {
        m_projectImagePath = projectImagePath;
    }

    public void setProjectName(String projectName)
    {
        m_projectName = projectName;
    }

    public void setProjectSummary(String projectSummary)
    {
        m_projectSummary = projectSummary;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public void setProjectAim(String projectAim)
    {
        m_projectAim = projectAim;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline)
    {
        m_applicationDeadline = applicationDeadline;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate)
    {
        m_expectedCompletionDate = expectedCompletionDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        m_startDate = startDate;
    }

    public void setCompletionDate(LocalDate completionDate)
    {
        m_completionDate = completionDate;
    }

    public void setMaxParticipant(int maxParticipant)
    {
        m_maxParticipant = maxParticipant;
    }

    public void setInviteLink(String inviteLink)
    {
        m_inviteLink = inviteLink;
    }

    public void setTechnicalRequirements(String technicalRequirements)
    {
        m_technicalRequirements = technicalRequirements;
    }

    public void setSpecialRequirements(String specialRequirements)
    {
        m_specialRequirements = specialRequirements;
    }

    public void setProjectAccessType(ProjectAccessType projectAccessType)
    {
        m_projectAccessType = projectAccessType;
    }

    public void setProfessionLevel(ProjectProfessionLevel professionLevel)
    {
        m_professionLevel = professionLevel;
    }

    public void setSector(Sector sector)
    {
        m_sector = sector;
    }

    public void setDegree(Degree degree)
    {
        m_degree = degree;
    }

    public void setProjectLevel(ProjectLevel projectLevel)
    {
        m_projectLevel = projectLevel;
    }

    public void setInterviewType(InterviewType interviewType)
    {
        m_interviewType = interviewType;
    }

    public void setProjectParticipants(Set<ProjectParticipant> projectParticipants)
    {
        m_projectParticipants = projectParticipants;
    }

    public void setProjectParticipantRequests(Set<ProjectParticipantRequest> projectParticipantRequests)
    {
        m_projectParticipantRequests = projectParticipantRequests;
    }

    public void setProjectStatus(EProjectStatus projectStatus)
    {
        m_projectStatus = projectStatus;
    }

    public void setFeedbackTimeRange(EFeedbackTimeRange feedbackTimeRange)
    {
        m_feedbackTimeRange = feedbackTimeRange;
    }
}
