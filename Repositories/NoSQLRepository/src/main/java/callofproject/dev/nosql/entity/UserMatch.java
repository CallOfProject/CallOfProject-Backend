/*----------------------------------------------------------------
	FILE		: UserMatch.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	UserMatch class represent the entity layer of the UserMatch entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("user_match")
@SuppressWarnings("all")
public class UserMatch
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "match_id")
    private UUID matchId;
    @Column(name = "user_id")
    private UUID userID;
    @Column(name = "school_id")
    private long schoolId;
    @Column(name = "course_id")
    private UUID courseId;
    @Column(name = "experience_id")
    private UUID experienceId;

    public UserMatch()
    {
    }

    public UserMatch(UUID matchId, UUID userID, long schoolId, UUID courseId, UUID experienceId)
    {
        this.matchId = matchId;
        this.userID = userID;
        this.schoolId = schoolId;
        this.courseId = courseId;
        this.experienceId = experienceId;
    }

    public UserMatch(UUID userID, long schoolId, UUID courseId, UUID experienceId)
    {
        this.userID = userID;
        this.schoolId = schoolId;
        this.courseId = courseId;
        this.experienceId = experienceId;
    }

    public UUID getMatchId()
    {
        return matchId;
    }

    public void setMatchId(UUID matchId)
    {
        this.matchId = matchId;
    }

    public UUID getUserID()
    {
        return userID;
    }

    public void setUserID(UUID userID)
    {
        this.userID = userID;
    }

    public long getSchoolId()
    {
        return schoolId;
    }

    public void setSchoolId(long schoolId)
    {
        this.schoolId = schoolId;
    }

    public UUID getCourseId()
    {
        return courseId;
    }

    public void setCourseId(UUID courseId)
    {
        this.courseId = courseId;
    }

    public UUID getExperienceId()
    {
        return experienceId;
    }

    public void setExperienceId(UUID experienceId)
    {
        this.experienceId = experienceId;
    }
}
