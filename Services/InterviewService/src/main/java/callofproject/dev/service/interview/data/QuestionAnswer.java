package callofproject.dev.service.interview.data;

import callofproject.dev.service.interview.data.entity.UserTestInterviews;
import jakarta.persistence.*;

@Entity
@Table(name = "question_answer")
public class QuestionAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_answer_id")
    private long m_id;

    @Column(name = "question_id")
    private long m_questionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_test_interview_id")
    private UserTestInterviews m_userTestInterviews;

    @Column(name = "answer")
    private String m_answer;

    public QuestionAnswer()
    {
    }

    public QuestionAnswer(long questionId, String answer)
    {
        m_questionId = questionId;
        m_answer = answer;
    }

    public QuestionAnswer(long questionId, UserTestInterviews userTestInterviews, String answer)
    {
        m_questionId = questionId;
        m_userTestInterviews = userTestInterviews;
        m_answer = answer;
    }

    public UserTestInterviews getUserTestInterviews()
    {
        return m_userTestInterviews;
    }

    public void setUserTestInterviews(UserTestInterviews userTestInterviews)
    {
        m_userTestInterviews = userTestInterviews;
    }

    public long getId()
    {
        return m_id;
    }

    public void setId(long id)
    {
        m_id = id;
    }

    public long getQuestionId()
    {
        return m_questionId;
    }

    public void setQuestionId(long questionId)
    {
        m_questionId = questionId;
    }

    public String getAnswer()
    {
        return m_answer;
    }

    public void setAnswer(String answer)
    {
        m_answer = answer;
    }
}
