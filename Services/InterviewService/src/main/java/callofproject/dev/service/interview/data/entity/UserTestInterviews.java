package callofproject.dev.service.interview.data.entity;

import callofproject.dev.service.interview.data.entity.enums.InterviewResult;
import callofproject.dev.service.interview.data.entity.enums.InterviewStatus;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_test_interviews")
@SuppressWarnings("all")
public class UserTestInterviews
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_test_interview_id")
    private UUID m_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User m_user;

    @ManyToOne
    @JoinColumn(name = "test_interview_id")
    private TestInterview m_testInterview;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_result")
    private InterviewResult m_interviewResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status")
    private InterviewStatus m_interviewStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> answers;

    public UserTestInterviews()
    {
        m_interviewResult = InterviewResult.NOT_COMPLETED;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public UserTestInterviews(User user, TestInterview testInterview)
    {
        m_user = user;
        m_testInterview = testInterview;
        m_interviewResult = InterviewResult.NOT_COMPLETED;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public UUID getId()
    {
        return m_id;
    }

    public void setId(UUID id)
    {
        m_id = id;
    }

    public User getUser()
    {
        return m_user;
    }

    public void setUser(User user)
    {
        m_user = user;
    }

    public TestInterview getTestInterview()
    {
        return m_testInterview;
    }

    public void setTestInterview(TestInterview testInterview)
    {
        m_testInterview = testInterview;
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

    public List<String> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<String> answers)
    {
        this.answers = answers;
    }
}