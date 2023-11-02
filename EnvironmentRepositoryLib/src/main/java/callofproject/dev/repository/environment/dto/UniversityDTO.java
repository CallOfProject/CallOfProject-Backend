package callofproject.dev.repository.environment.dto;

public class UniversityDTO
{
    private String universityName;
    private long universityId;

    public UniversityDTO(String universityName, long universityId)
    {
        this.universityName = universityName.toUpperCase();
        this.universityId = universityId;
    }

    public String getUniversityName()
    {
        return universityName;
    }

    public long getUniversityId()
    {
        return universityId;
    }

}
