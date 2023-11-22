package callofproject.dev.repository.repository.project.entity;

import callofproject.dev.repository.repository.project.entity.enums.EInterviewType;
import jakarta.persistence.*;

@Entity
@Table(name = "interview_type")
public class InterviewType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_type_id")
    private long m_interviewTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type_name", unique = true)
    private EInterviewType m_interviewType;

    public InterviewType()
    {
    }

    public InterviewType(EInterviewType interviewType)
    {
        m_interviewType = interviewType;
    }

    public long getInterviewTypeId()
    {
        return m_interviewTypeId;
    }

    public void setInterviewTypeId(long interviewTypeId)
    {
        m_interviewTypeId = interviewTypeId;
    }

    public EInterviewType getInterviewType()
    {
        return m_interviewType;
    }

    public void setInterviewType(EInterviewType interviewType)
    {
        m_interviewType = interviewType;
    }
}
