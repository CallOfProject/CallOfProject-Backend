package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UniversitySaveDTO
{
    @JsonProperty("university_name")
    private String universityName;

    public UniversitySaveDTO()
    {
    }

    public UniversitySaveDTO(String universityName)
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
