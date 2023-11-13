package nuricanozturk.dev.data.entity;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "house")
public class House
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "house_id")
    private UUID m_houseId;

    private String m_description;
    private String m_houseName;
    @OneToOne(cascade = CascadeType.ALL)
    private Address m_address;
    @OneToMany(mappedBy = "m_house", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> m_reservations;

    public House()
    {
    }

    public House(String description, String houseName)
    {
        m_description = description;
        m_houseName = houseName;
    }

    public House(String description, String houseName, Address address)
    {
        m_description = description;
        m_houseName = houseName;
        m_address = address;
    }

    public House(String description, String houseName, Address address, Set<Reservation> reservations)
    {
        m_description = description;
        m_houseName = houseName;
        m_address = address;
        m_reservations = reservations;
    }

    public UUID getHouseId()
    {
        return m_houseId;
    }

    public void setHouseId(UUID houseId)
    {
        m_houseId = houseId;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public String getHouseName()
    {
        return m_houseName;
    }

    public void setHouseName(String houseName)
    {
        m_houseName = houseName;
    }

    public Address getAddress()
    {
        return m_address;
    }

    public void setAddress(Address address)
    {
        m_address = address;
    }

    public Set<Reservation> getReservations()
    {
        return m_reservations;
    }

    public void setReservations(Set<Reservation> reservations)
    {
        m_reservations = reservations;
    }
}
