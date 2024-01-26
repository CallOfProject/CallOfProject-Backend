package callofproject.dev.environment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UniversityDTO
 */
public class UniversityDTO
{
    @JsonProperty("university_name")
    private String universityName;

    private String id;

    /**
     * Constructor
     */
    public UniversityDTO()
    {
    }

    /**
     * Constructor
     *
     * @param universityName university name
     * @param id             id
     */
    public UniversityDTO(String universityName, String id)
    {
        this.universityName = universityName;
        this.id = id;
    }

    /**
     * Constructor
     *
     * @param universityName university name
     */
    public UniversityDTO(String universityName)
    {
        this.universityName = universityName;
    }

    /**
     * Getter
     *
     * @return id
     */
    public String getId()
    {
        return id;
    }


    /**
     * Setter
     *
     * @param id id
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Getter
     *
     * @return university name
     */
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
