package callofproject.dev.data.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private LocalDate m_applicationDeadline;
    @Column(name = "expected_completion_date", nullable = false)
    private LocalDate m_expectedCompletionDate;
    @Column(name = "expected_project_deadline", nullable = false)
    private LocalDate m_expectedProjectDeadline;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User m_projectOwner;

    @OneToMany(mappedBy = "m_project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProjectParticipant> m_projectParticipants;


    public Project()
    {
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

    public User getProjectOwner()
    {
        return m_projectOwner;
    }

    public Set<ProjectParticipant> getProjectParticipants()
    {
        return m_projectParticipants;
    }

    public static class Builder
    {
        private final Project m_project;

        public Builder()
        {
            m_project = new Project();
        }

        public Builder setProjectOwner(User projectOwner)
        {
            m_project.m_projectOwner = projectOwner;
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

        public Builder setExpectedProjectDeadline(LocalDate expectedProjectDeadline)
        {
            m_project.m_expectedProjectDeadline = expectedProjectDeadline;
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

    public LocalDate getExpectedProjectDeadline()
    {
        return m_expectedProjectDeadline;
    }

    public int getMaxParticipant()
    {
        return m_maxParticipant;
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
}
