package callofproject.dev.repository.environment.dto;

public record UniversityDTO(String universityName, long universityId)
{
    public UniversityDTO(String universityName, long universityId)
    {
        this.universityName = universityName.toUpperCase();
        this.universityId = universityId;
    }
}
