package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UniversitySaveDTO
 */
public class UniversitySaveDTO
{
    @JsonProperty("university_name")
    private String universityName;

    /**
     * Constructor
     */
    public UniversitySaveDTO()
    {
    }

    /**
     * Constructor
     *
     * @param universityName university name
     */
    public UniversitySaveDTO(String universityName)
    {
        this.universityName = universityName;
    }

    /**
     * Getter
     *
     * @return university name
     */
    @JsonProperty("university_name")
    public String getUniversityName()
    {
        return universityName;
    }

    /**
     * Setter
     *
     * @param universityName university name
     */
    public void setUniversityName(String universityName)
    {
        this.universityName = universityName;
    }

}
