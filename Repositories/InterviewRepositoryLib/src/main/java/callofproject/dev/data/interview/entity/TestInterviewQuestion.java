package callofproject.dev.data.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_interview_question")
public class TestInterviewQuestion implements Comparable<TestInterviewQuestion>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_question_id")
    private long m_id;

    @Column(name = "title")
    private String m_title;

    @Column(name = "point")
    private int m_point;

    @Column(name = "question")
    private String m_question;

    @Column(name = "option1")
    private String m_option1;

    @Column(name = "option2")
    private String m_option2;

    @Column(name = "option3")
    private String m_option3;

    @Column(name = "option4")
    private String m_option4;

    @Column(name = "answer")
    private String m_answer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_interview_id")
    private TestInterview m_testInterview;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QuestionStatus m_status;

    public TestInterviewQuestion()
    {
        m_status = QuestionStatus.UNANSWERED;
    }

    public TestInterviewQuestion(String title, int point, String question)
    {
        m_title = title;
        m_point = point;
        m_question = question;
        m_status = QuestionStatus.UNANSWERED;
    }

    public TestInterviewQuestion(String title, int point, String question, String option1, String option2, String option3, String option4, String answer, TestInterview testInterview, QuestionStatus status)
    {
        m_title = title;
        m_point = point;
        m_question = question;
        m_option1 = option1;
        m_option2 = option2;
        m_option3 = option3;
        m_option4 = option4;
        m_answer = answer;
        m_testInterview = testInterview;
        m_status = status;
    }

    @Override
    public int compareTo(TestInterviewQuestion other)
    {
        return Long.compare(m_id, other.m_id);
    }

    public static class Builder
    {
        private final TestInterviewQuestion m_question;

        public Builder(String title, int point, String question)
        {
            m_question = new TestInterviewQuestion(title, point, question);
        }

        public Builder setOption1(String option1)
        {
            m_question.m_option1 = option1;
            return this;
        }

        public Builder setOption2(String option2)
        {
            m_question.m_option2 = option2;
            return this;
        }

        public Builder setOption3(String option3)
        {
            m_question.m_option3 = option3;
            return this;
        }

        public Builder setOption4(String option4)
        {
            m_question.m_option4 = option4;
            return this;
        }

        public Builder setAnswer(String answer)
        {
            m_question.m_answer = answer;
            return this;
        }

        public Builder setTestInterview(TestInterview testInterview)
        {
            m_question.m_testInterview = testInterview;
            return this;
        }

        public TestInterviewQuestion build()
        {
            return m_question;
        }
    }

    public QuestionStatus getStatus()
    {
        return m_status;
    }

    public void setStatus(QuestionStatus status)
    {
        m_status = status;
    }

    public long getId()
    {
        return m_id;
    }

    public void setId(long id)
    {
        m_id = id;
    }

    public String getTitle()
    {
        return m_title;
    }

    public void setTitle(String title)
    {
        m_title = title;
    }

    public int getPoint()
    {
        return m_point;
    }

    public void setPoint(int point)
    {
        m_point = point;
    }

    public String getQuestion()
    {
        return m_question;
    }

    public void setQuestion(String question)
    {
        m_question = question;
    }

    public String getOption1()
    {
        return m_option1;
    }

    public void setOption1(String option1)
    {
        m_option1 = option1;
    }

    public String getOption2()
    {
        return m_option2;
    }

    public void setOption2(String option2)
    {
        m_option2 = option2;
    }

    public String getOption3()
    {
        return m_option3;
    }

    public void setOption3(String option3)
    {
        m_option3 = option3;
    }

    public String getOption4()
    {
        return m_option4;
    }

    public void setOption4(String option4)
    {
        m_option4 = option4;
    }

    public String getAnswer()
    {
        return m_answer;
    }

    public void setAnswer(String answer)
    {
        m_answer = answer;
    }

    public TestInterview getTestInterview()
    {
        return m_testInterview;
    }

    public void setTestInterview(TestInterview testInterview)
    {
        m_testInterview = testInterview;
    }
}
