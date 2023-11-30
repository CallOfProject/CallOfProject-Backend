/*----------------------------------------------------------------
	FILE		: UserMatch.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	UserMatch class represent the entity layer of the UserMatch entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * UserMatch class represent the entity layer of the UserMatch entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Document("user_match")
@SuppressWarnings("all")
public class UserMatch
{
    @Id
    private String id;
    private UUID userID;
    private long schoolId;
    private UUID courseId;
    private UUID experienceId;

    /**
     * Default Constructor
     */
    public UserMatch()
    {
    }

    /**
     * Constructor
     *
     * @param matchId      match id
     * @param userID       user id
     * @param schoolId     school id
     * @param courseId     course id
     * @param experienceId experience id
     */
    public UserMatch(String matchId, UUID userID, long schoolId, UUID courseId, UUID experienceId)
    {
        this.id = matchId;
        this.userID = userID;
        this.schoolId = schoolId;
        this.courseId = courseId;
        this.experienceId = experienceId;
    }

    /**
     * Constructor
     *
     * @param userID       user id
     * @param schoolId     school id
     * @param courseId     course id
     * @param experienceId experience id
     */
    public UserMatch(UUID userID, long schoolId, UUID courseId, UUID experienceId)
    {
        this.userID = userID;
        this.schoolId = schoolId;
        this.courseId = courseId;
        this.experienceId = experienceId;
    }

    /**
     * Get match id
     *
     * @return match id
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set match id
     *
     * @param id match id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Get user id
     *
     * @return user id
     */
    public UUID getUserID()
    {
        return userID;
    }

    /**
     * Set user id
     *
     * @param userID user id
     */
    public void setUserID(UUID userID)
    {
        this.userID = userID;
    }

    /**
     * Get school id
     *
     * @return school id
     */
    public long getSchoolId()
    {
        return schoolId;
    }

    /**
     * Set school id
     *
     * @param schoolId school id
     */
    public void setSchoolId(long schoolId)
    {
        this.schoolId = schoolId;
    }

    /**
     * Get course id
     *
     * @return course id
     */
    public UUID getCourseId()
    {
        return courseId;
    }

    /**
     * Set course id
     *
     * @param courseId course id
     */
    public void setCourseId(UUID courseId)
    {
        this.courseId = courseId;
    }

    /**
     * Get experience id
     *
     * @return experience id
     */
    public UUID getExperienceId()
    {
        return experienceId;
    }

    /**
     * Set experience id
     *
     * @param experienceId experience id
     */
    public void setExperienceId(UUID experienceId)
    {
        this.experienceId = experienceId;
    }
}
