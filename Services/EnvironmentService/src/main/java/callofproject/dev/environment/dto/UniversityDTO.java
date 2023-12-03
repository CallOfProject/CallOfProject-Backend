package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UniversityDTO
{
    @JsonProperty("university_name")
    private String universityName;

    private String id;
    public UniversityDTO()
    {
    }

    public UniversityDTO(String universityName, String id)
    {
        this.universityName = universityName;
        this.id = id;
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
