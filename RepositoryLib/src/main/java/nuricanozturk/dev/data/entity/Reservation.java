package nuricanozturk.dev.data.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reservation")
public class Reservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reservatiom_id")
    private UUID m_addressId;
    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House m_house;
    @Column(name = "start_date")
    private LocalDate m_startDate;
    @Column(name = "finish_date")
    private LocalDate m_finishDate;

    public Reservation()
    {
    }

    public Reservation(House house, LocalDate startDate, LocalDate finishDate)
    {
        m_house = house;
        m_startDate = startDate;
        m_finishDate = finishDate;
    }

    public Reservation(LocalDate startDate, LocalDate finishDate)
    {
        m_startDate = startDate;
        m_finishDate = finishDate;
    }
}
