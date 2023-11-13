package nuricanozturk.dev.data.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "address")
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID m_addressId;
    @Column(name = "city")
    private String m_city;
    @Column(name = "country")
    private String m_country;
    @Column(name = "street")
    private String m_street;
    @OneToOne(mappedBy = "m_address")
    private House m_house;

    public Address()
    {
    }

    public Address(String city, String country, String street)
    {
        m_city = city;
        m_country = country;
        m_street = street;
    }

    public Address(String city, String country, String street, House house)
    {
        m_city = city;
        m_country = country;
        m_street = street;
        m_house = house;
    }

    public UUID getAddressId()
    {
        return m_addressId;
    }

    public void setAddressId(UUID addressId)
    {
        m_addressId = addressId;
    }

    public String getCity()
    {
        return m_city;
    }

    public void setCity(String city)
    {
        m_city = city;
    }

    public String getCountry()
    {
        return m_country;
    }

    public void setCountry(String country)
    {
        m_country = country;
    }

    public String getStreet()
    {
        return m_street;
    }

    public void setStreet(String street)
    {
        m_street = street;
    }

    public House getHouse()
    {
        return m_house;
    }

    public void setHouse(House house)
    {
        m_house = house;
    }
}
