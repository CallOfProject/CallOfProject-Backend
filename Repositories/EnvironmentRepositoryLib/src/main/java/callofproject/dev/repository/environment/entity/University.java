package callofproject.dev.repository.environment.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("university")
public class University
{
    @Id
    private String id;
    @Indexed(unique = true)
    private String universityName;

    public University(String universityName)
    {
        this.universityName = universityName;
    }

    public University()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUniversityName()
    {
        return universityName;
    }

    public void setUniversityName(String universityName)
    {
        this.universityName = universityName;
    }
}
