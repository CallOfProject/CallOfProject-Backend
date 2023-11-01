package callofproject.dev.repository.environment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "university", schema = "cop_environment_db")
public class University
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private long universityId;
    @Column(name = "university_name", unique = true, nullable = false)
    private String universityName;

    public University(String universityName)
    {
        this.universityName = universityName;
    }

    public University()
    {

    }

    public long getUniversityId()
    {
        return universityId;
    }

    public void setUniversityId(long universityId)
    {
        this.universityId = universityId;
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
