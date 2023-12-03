package callofproject.dev.repository.environment.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("company")
public class Company
{
    @Id
    private String companyId;

    @Indexed(unique = true)
    private String companyName;

    public Company()
    {
    }

    public Company(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
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
