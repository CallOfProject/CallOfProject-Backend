package callofproject.dev.authentication.dto.client;


/**
 * Data Transfer Object for a university.
 */
public class UniversityDTO
{
    private String universityName;
    private String id;


    /**
     * Constructor.
     */
    public UniversityDTO()
    {
    }


    /**
     * Get id
     *
     * @return id
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set id
     *
     * @param id id
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Constructor.
     *
     * @param universityName The university name.
     */
    public UniversityDTO(String universityName)
    {
        this.universityName = universityName;
    }


    /**
     * Gets the university name.
     *
     * @return The university name.
     */
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