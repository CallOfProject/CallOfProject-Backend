package callofproject.dev.usermanagement;

public class UniversityDTO
{
    private String universityName;
    private long universityId;

    public UniversityDTO()
    {
    }

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


    public void setUniversityName(String universityName)
    {
        this.universityName = universityName;
    }

    public void setUniversityId(long universityId)
    {
        this.universityId = universityId;
    }
}
