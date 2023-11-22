package callofproject.dev.repository.repository.project.entity;

import callofproject.dev.repository.repository.project.entity.enums.EDegree;
import jakarta.persistence.*;

@Entity
@Table(name = "degree")
public class Degree
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "degree_id")
    private long m_degreeId;
    @Enumerated(EnumType.STRING)
    @Column(name = "degree_name", unique = true)
    private EDegree m_degree;

    public Degree()
    {
    }

    public Degree(EDegree degree)
    {
        m_degree = degree;
    }

    public long getDegreeId()
    {
        return m_degreeId;
    }

    public void setDegreeId(long degreeId)
    {
        m_degreeId = degreeId;
    }

    public EDegree getDegree()
    {
        return m_degree;
    }

    public void setDegree(EDegree degree)
    {
        m_degree = degree;
    }
}
