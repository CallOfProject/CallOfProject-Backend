package callofproject.dev.service.ticket.entity;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.common.status.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
    private String username;
    private String title;
    private String topic;
    private String description;
    private LocalDate date;
    @Enumerated(value = EnumType.STRING)
    private EOperation status;

    public Ticket(String username, UUID userId, String title, String topic, String description, EOperation status)
    {
        this.username = username;
        this.userId = userId;
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.date = now();
        this.status = status;
    }

    public Ticket()
    {
        date = now();
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

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public EOperation getStatus()
    {
        return status;
    }

    public void setStatus(EOperation status)
    {
        this.status = status;
    }
}
