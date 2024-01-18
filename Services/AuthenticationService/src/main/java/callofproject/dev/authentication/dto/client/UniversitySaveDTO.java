package callofproject.dev.authentication.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for a university.
 */
public class UniversitySaveDTO
{
    @JsonProperty("university_name")
    private String universityName;


    /**
     * Constructor.
     */
    public UniversitySaveDTO()
    {
    }


    /**
     * Constructor.
     *
     * @param universityName The university name.
     */
    public UniversitySaveDTO(String universityName)
    {
        this.universityName = universityName;
    }


    /**
     * Gets the university name.
     *
     * @return The university name.
     */
    @JsonProperty("university_name")
    public String getUniversityName()
    {
        return universityName;
    }


    /**
     * Sets the university name.
     *
     * @param universityName The university name.
     */
    public void setUniversityName(String universityName)
    {
        this.universityName = universityName;
    }

}