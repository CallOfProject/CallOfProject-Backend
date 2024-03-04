package callofproject.dev.data.interview.entity;


import callofproject.dev.data.interview.entity.enums.InterviewResult;
import callofproject.dev.data.interview.entity.enums.InterviewStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_coding_interviews")
@SuppressWarnings("all")
public class UserCodingInterviews
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_coding_interview_id")
    private UUID m_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User m_user;

    @ManyToOne
    @JoinColumn(name = "coding_interview_id")
    private CodingInterview m_codingInterview;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_result")
    private InterviewResult m_interviewResult;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_status", length = 20)
    private InterviewStatus m_interviewStatus;

    @Column(name = "answer_file")
    private String m_answerFileName;

    @Column(name = "answer_file_url")
    private String m_answerFileUrl;

    public UserCodingInterviews()
    {
        m_interviewResult = InterviewResult.NOT_COMPLETED;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public UserCodingInterviews(User user, CodingInterview codingInterview)
    {
        m_user = user;
        m_codingInterview = codingInterview;
        m_interviewResult = InterviewResult.NOT_COMPLETED;
        m_interviewStatus = InterviewStatus.NOT_STARTED;
    }

    public String getAnswerFileUrl()
    {
        return m_answerFileUrl;
    }

    public void setAnswerFileUrl(String answerFileUrl)
    {
        m_answerFileUrl = answerFileUrl;
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

    public CodingInterview getCodingInterview()
    {
        return m_codingInterview;
    }

    public void setCodingInterview(CodingInterview codingInterview)
    {
        m_codingInterview = codingInterview;
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

    public String getAnswerFileName()
    {
        return m_answerFileName;
    }

    public void setAnswerFileName(String answerFileName)
    {
        m_answerFileName = answerFileName;
    }
}