package callofproject.dev.repository.environment.dto;

public class UniversityDTO
{
    private String universityName;

    public UniversityDTO(String universityName)
    {
        this.universityName = universityName.toUpperCase();
    }

    public String getUniversityName()
    {
        return universityName;
    }


}
