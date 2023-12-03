package callofproject.dev.repository.environment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("company")
public class Company
{
    @Id
    private String id;

    @Indexed(unique = true)
    @JsonProperty("company_name")
    private String companyName;

    public Company()
    {
    }

    public Company(String companyName)
    {
        this.companyName = companyName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
}
