package callofproject.dev.repository.repository.project.entity;

import callofproject.dev.repository.repository.project.entity.enums.ESector;
import jakarta.persistence.*;

@Entity
@Table(name = "sector")
public class Sector
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sector_id")
    private long m_sectorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sector_name", unique = true)
    private ESector m_sector;

    public Sector()
    {
    }

    public Sector(ESector sector)
    {
        m_sector = sector;
    }

    public long getSectorId()
    {
        return m_sectorId;
    }

    public void setSectorId(long sectorId)
    {
        m_sectorId = sectorId;
    }

    public ESector getSector()
    {
        return m_sector;
    }

    public void setSector(ESector sector)
    {
        m_sector = sector;
    }
}
