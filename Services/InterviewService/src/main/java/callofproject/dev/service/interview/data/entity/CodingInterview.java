package callofproject.dev.service.interview.data.entity;

import callofproject.dev.service.interview.data.entity.enums.InterviewResult;
import callofproject.dev.service.interview.data.entity.enums.InterviewStatus;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "coding_interview")
public class CodingInterview
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coding_interview_id")
    private UUID m_codingInterviewId;

    @Column(name = "title", nullable = false)
    private String m_title;

    @Column(name = "description", nullable = false)
    private String m_description;

    @Column(name = "duration_minutes", nullable = false)
    private long m_durationMinutes;

    @Column(name = "question", nullable = false)
    private String m_question;

    @Column(name = "question_file_name")
    private String m_answerFileName;

    @Column(name = "point", nullable = false)
    private int m_point;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_result")
    private InterviewResult m_interviewResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status")
    private InterviewStatus m_interviewStatus;

    @OneToOne(mappedBy = "m_codingInterview", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Project project;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "m_codingInterviews")
    private Set<User> m_assignedUsers;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy kk:mm:ss")
    private LocalDateTime m_startTime;

    @Column(name = "end_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy kk:mm:ss")
    private LocalDateTime m_endTime;

    public CodingInterview()
    {
        m_interviewResult = InterviewResult.NOT_COMPLETED;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public CodingInterview(String title, String description, long durationMinutes, String question, String answerFileName, int point, Project project)
    {
        this.project = project;
        m_title = title;
        m_description = description;
        m_durationMinutes = durationMinutes;
        m_question = question;
        m_answerFileName = answerFileName;
        m_point = point;
    }

    public void addAssignedUser(User user)
    {
        if (m_assignedUsers == null)
            m_assignedUsers = new HashSet<>();

        m_assignedUsers.add(user);
    }

    public UUID getCodingInterviewId()
    {
        return m_codingInterviewId;
    }

    public void setCodingInterviewId(UUID codingInterviewId)
    {
        m_codingInterviewId = codingInterviewId;
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

    public long getDurationMinutes()
    {
        return m_durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes)
    {
        m_durationMinutes = durationMinutes;
    }

    public String getQuestion()
    {
        return m_question;
    }

    public void setQuestion(String question)
    {
        m_question = question;
    }

    public String getAnswerFileName()
    {
        return m_answerFileName;
    }

    public void setAnswerFileName(String answerFileName)
    {
        m_answerFileName = answerFileName;
    }

    public int getPoint()
    {
        return m_point;
    }

    public void setPoint(int point)
    {
        m_point = point;
    }

    public InterviewResult getInterviewResult()
    {
        return m_interviewResult;
    }

    public void setInterviewResult(InterviewResult interviewResult)
    {
        m_interviewResult = interviewResult;
    }

    public InterviewStatus getInterviewStatus()
    {
        return m_interviewStatus;
    }

    public void setInterviewStatus(InterviewStatus interviewStatus)
    {
        m_interviewStatus = interviewStatus;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public Set<User> getAssignedUsers()
    {
        return m_assignedUsers;
    }

    public void setAssignedUsers(Set<User> assignedUsers)
    {
        m_assignedUsers = assignedUsers;
    }
}
