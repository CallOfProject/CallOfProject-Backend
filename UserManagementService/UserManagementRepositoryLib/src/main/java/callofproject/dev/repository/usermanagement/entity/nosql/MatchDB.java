package callofproject.dev.repository.usermanagement.entity.nosql;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class MatchDB
{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID matchId;
    private UUID userID;
    private String schoolName;
    private String CourseName;

    public MatchDB()
    {
    }

    public MatchDB(UUID matchId, UUID userID, String schoolName, String courseName)
    {
        this.matchId = matchId;
        this.userID = userID;
        this.schoolName = schoolName;
        CourseName = courseName;
    }

    public MatchDB(UUID userID, String schoolName, String courseName)
    {
        this.userID = userID;
        this.schoolName = schoolName;
        CourseName = courseName;
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

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
    }

    public String getCourseName()
    {
        return CourseName;
    }

    public void setCourseName(String courseName)
    {
        CourseName = courseName;
    }
}
