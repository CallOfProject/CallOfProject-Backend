package callofproject.dev.repository.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UniversityDTO
{
    @JsonProperty("university_name")
    private String universityName;

    public UniversityDTO()
    {
    }

    public UniversityDTO(String universityName)
    {
        this.universityName = universityName;
    }
    @JsonProperty("university_name")
    public String getUniversityName()
    {
        return universityName;
    }

    public void setUniversityName(String universityName)
    {
        this.universityName = universityName;
    }

}
