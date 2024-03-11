package callofproject.dev.data.interview.entity;


import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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

    @Column(name = "question", nullable = false, length = 2000)
    private String m_question;

    @Column(name = "point", nullable = false)
    private int m_point;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status")
    private InterviewStatus m_interviewStatus;

    @OneToOne(mappedBy = "m_codingInterview", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Project project;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy kk:mm:ss")
    private LocalDateTime m_startTime;

    @Column(name = "end_time")
    @DateTimeFormat(pattern = "dd/MM/yyyy kk:mm:ss")
    private LocalDateTime m_endTime;

    @OneToMany(mappedBy = "m_codingInterview", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserCodingInterviews> m_codingInterviews;

    public CodingInterview()
    {
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public CodingInterview(String title, String description, long durationMinutes, String question, int point, Project project,
                           LocalDateTime startTime, LocalDateTime endTime)
    {
        this.project = project;
        m_title = title;
        m_description = description;
        m_durationMinutes = durationMinutes;
        m_question = question;
        m_point = point;
        m_startTime = startTime;
        m_endTime = endTime;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }


    public LocalDateTime getStartTime()
    {
        return m_startTime;
    }

    public Set<UserCodingInterviews> getCodingInterviews()
    {
        return m_codingInterviews;
    }

    public void setCodingInterviews(Set<UserCodingInterviews> codingInterviews)
    {
        m_codingInterviews = codingInterviews;
    }

    public void setStartTime(LocalDateTime startTime)
    {
        m_startTime = startTime;
    }

    public LocalDateTime getEndTime()
    {
        return m_endTime;
    }

    public void setEndTime(LocalDateTime endTime)
    {
        m_endTime = endTime;
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

    public int getPoint()
    {
        return m_point;
    }

    public void setPoint(int point)
    {
        m_point = point;
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
}
