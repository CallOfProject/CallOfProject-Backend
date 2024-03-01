package callofproject.dev.service.interview.data.entity;


import callofproject.dev.service.interview.data.entity.enums.InterviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "test_interview")
public class TestInterview
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "test_interview_id")
    private UUID m_id;

    @Column(name = "question_count")
    private int m_questionCount;

    @Column(name = "title")
    private String m_title;

    @Column(name = "total_time_minutes")
    private long m_totalTimeMinutes;

    @Column(name = "description")
    private String m_description;

    @Column(name = "total_score")
    private int m_totalScore = 100;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status")
    private InterviewStatus m_interviewStatus;

    @OneToMany(mappedBy = "m_testInterview", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserTestInterviews> m_testInterviews;

    @OneToOne(mappedBy = "m_testInterview", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private Project project;

    @OneToMany(mappedBy = "m_testInterview", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TestInterviewQuestion> m_questions;


    public TestInterview()
    {
        m_interviewStatus = InterviewStatus.NOT_STARTED;

    }


    public TestInterview(int questionCount, String title, long totalTimeMinutes, String description)
    {
        m_questionCount = questionCount;
        m_title = title;
        m_totalTimeMinutes = totalTimeMinutes;
        m_description = description;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public TestInterview(int questionCount, String title, long totalTimeMinutes, String description, int totalScore,
                         InterviewStatus interviewStatus)
    {
        m_questionCount = questionCount;
        m_title = title;
        m_totalTimeMinutes = totalTimeMinutes;
        m_description = description;
        m_totalScore = totalScore;
        m_interviewStatus = interviewStatus;
    }

    public void addQuestion(TestInterviewQuestion question)
    {
        if (m_questions == null)
            m_questions = new HashSet<>();

        m_questions.add(question);
    }

    public Set<UserTestInterviews> getTestInterviews()
    {
        return m_testInterviews;
    }

    public void setTestInterviews(Set<UserTestInterviews> testInterviews)
    {
        m_testInterviews = testInterviews;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public UUID getId()
    {
        return m_id;
    }

    public void setId(UUID id)
    {
        m_id = id;
    }

    public int getQuestionCount()
    {
        return m_questionCount;
    }

    public void setQuestionCount(int questionCount)
    {
        m_questionCount = questionCount;
    }

    public String getTitle()
    {
        return m_title;
    }

    public void setTitle(String title)
    {
        m_title = title;
    }

    public long getTotalTimeMinutes()
    {
        return m_totalTimeMinutes;
    }

    public void setTotalTimeMinutes(long totalTimeMinutes)
    {
        m_totalTimeMinutes = totalTimeMinutes;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public int getTotalScore()
    {
        return m_totalScore;
    }

    public void setTotalScore(int totalScore)
    {
        m_totalScore = totalScore;
    }

    public InterviewStatus getInterviewStatus()
    {
        return m_interviewStatus;
    }

    public void setInterviewStatus(InterviewStatus interviewStatus)
    {
        m_interviewStatus = interviewStatus;
    }

    public Set<TestInterviewQuestion> getQuestions()
    {
        return m_questions;
    }

    public void setQuestions(Set<TestInterviewQuestion> questions)
    {
        m_questions = questions;
    }
}