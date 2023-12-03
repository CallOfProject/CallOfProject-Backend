package callofproject.dev.authentication.dto.client;

public class UniversityDTO
{
    private String universityName;
    private String id;

    public UniversityDTO()
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

    public UniversityDTO(String universityName)
    {
        this.universityName = universityName;
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