package callofproject.dev.service.ticket.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

import static java.time.LocalDate.now;

@Document(collection = "ticket")
public class Ticket
{
    @Id
    private String id;
    private UUID userId;
    private UUID adminId;
    private String adminUsername;
    private String username;
    private String title;
    private LocalDate feedbackDeadline;
    private String description;
    private String userEmail;
    private String answer;
    private LocalDate answeredDate;
    private LocalDate createdDate;
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    public Ticket()
    {
        this.feedbackDeadline = now().plusDays(7);
        this.status = TicketStatus.OPEN;
        this.createdDate = now();
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public LocalDate getFeedbackDeadline()
    {
        return feedbackDeadline;
    }

    public void setFeedbackDeadline(LocalDate feedbackDeadline)
    {
        this.feedbackDeadline = feedbackDeadline;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getAdminUsername()
    {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername)
    {
        this.adminUsername = adminUsername;
    }

    public UUID getAdminId()
    {
        return adminId;
    }

    public void setAdminId(UUID adminId)
    {
        this.adminId = adminId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public LocalDate getFeedbackDeadlineDay()
    {
        return feedbackDeadline;
    }

    public void setFeedbackDeadlineDay(LocalDate feedbackDeadlineDay)
    {
        this.feedbackDeadline = feedbackDeadlineDay;
    }

    public LocalDate getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getAnsweredDate()
    {
        return answeredDate;
    }

    public void setAnsweredDate(LocalDate answeredDate)
    {
        this.answeredDate = answeredDate;
    }

    public TicketStatus getStatus()
    {
        return status;
    }

    public void setStatus(TicketStatus status)
    {
        this.status = status;
    }
}
