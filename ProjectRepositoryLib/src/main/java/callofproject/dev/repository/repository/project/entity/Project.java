package callofproject.dev.repository.repository.project.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "project")
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id")
    private UUID m_projectId;

    @Column(name = "project_image", nullable = true)
    private String m_projectImagePath;

    @Column(name = "title", nullable = false)
    private String m_projectName;

    @Column(name = "summary", nullable = false)
    private String m_projectSummary;

    @Column(name = "description", nullable = false)
    private String m_description;

    @Column(name = "aim", nullable = false)
    private String m_aim;

    @Column(name = "application_deadline", nullable = false)
    private LocalDate m_applicationDeadline;

    @Column(name = "expected_completion_date", nullable = false)
    private LocalDate m_expectedCompletionDate;
    @Column(name = "expected_project_deadline", nullable = false)
    private LocalDate m_expectedProjectDeadline;

    private int m_maxParticipant;

    @Enumerated(EnumType.STRING)

    private ProjectAccessType m_projectAccessType;

    @Enumerated(EnumType.STRING)
    private ProjectProfessionLevel m_professionLevel;

    private String m_inviteLink;


    public Project(String projectImagePath, String projectName, String projectSummary, String description, String aim, LocalDate applicationDeadline, LocalDate expectedCompletionDate, LocalDate expectedProjectDeadline, int maxParticipant, ProjectAccessType projectAccessType, ProjectProfessionLevel professionLevel, String inviteLink)
    {
        m_projectImagePath = projectImagePath;
        m_projectName = projectName;
        m_projectSummary = projectSummary;
        m_description = description;
        m_aim = aim;
        m_applicationDeadline = applicationDeadline;
        m_expectedCompletionDate = expectedCompletionDate;
        m_expectedProjectDeadline = expectedProjectDeadline;
        m_maxParticipant = maxParticipant;
        m_projectAccessType = projectAccessType;
        m_professionLevel = professionLevel;
        m_inviteLink = inviteLink;
    }
}
