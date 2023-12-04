package callofproject.dev.service.ticket.dto;

import callofproject.dev.data.common.enums.EOperation;

import java.util.UUID;

public class TicketDTO
{
    private UUID userId;
    private String username;
    private String title;
    private String topic;
    private String description;
    private EOperation status;

    public TicketDTO()
    {
    }

    public TicketDTO(UUID userId, String username, String title, String topic, String description, EOperation status)
    {
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.status = status;
    }

    public UUID getUserId()
    {
        return userId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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

    public EOperation getStatus()
    {
        return status;
    }

    public void setStatus(EOperation status)
    {
        this.status = status;
    }
}
