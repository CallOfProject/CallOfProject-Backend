package callofproject.dev.repository.usermanagement.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_rate")
public class UserRate
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rate_id")
    private UUID rate_id;
    @Column(name = "user_rate", nullable = false)
    private double userRate;
    @Column(name = "user_feedback_rate", nullable = false)
    private double userFeedbackRate;

    public UserRate()
    {
        userRate = 0.0;
        userFeedbackRate = 0.0;
    }

    public UUID getRate_id()
    {
        return rate_id;
    }

    public void setRate_id(UUID rate_id)
    {
        this.rate_id = rate_id;
    }

    public double getUserRate()
    {
        return userRate;
    }

    public void setUserRate(double userRate)
    {
        this.userRate = userRate;
    }

    public double getUserFeedbackRate()
    {
        return userFeedbackRate;
    }

    public void setUserFeedbackRate(double userFeedbackRate)
    {
        this.userFeedbackRate = userFeedbackRate;
    }
}
