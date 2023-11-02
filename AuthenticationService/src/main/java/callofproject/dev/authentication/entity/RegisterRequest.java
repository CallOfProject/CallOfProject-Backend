package callofproject.dev.authentication.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class RegisterRequest
{

    @JsonProperty("first_name")
    private String m_firstName;
    @JsonProperty("last_name")
    private String m_lastName;
    @JsonProperty("middle_name")
    private String m_middleName;
    @JsonProperty("username")
    private String m_username;
    @JsonProperty("email")
    private String email;
    private String password;
    @JsonProperty("birth_date")
    private LocalDate m_birthDate;

    public RegisterRequest(String firstName, String lastName, String middleName, String username, String email, String password, LocalDate birthDate)
    {
        m_firstName = firstName;
        m_lastName = lastName;
        m_middleName = middleName;
        m_username = username;
        this.email = email;
        this.password = password;
        m_birthDate = birthDate;
    }

    public String getFirstName()
    {
        return m_firstName;
    }

    public void setFirstName(String firstName)
    {
        m_firstName = firstName;
    }

    public String getLastName()
    {
        return m_lastName;
    }

    public void setLastName(String lastName)
    {
        m_lastName = lastName;
    }

    public String getMiddleName()
    {
        return m_middleName;
    }

    public void setMiddleName(String middleName)
    {
        m_middleName = middleName;
    }

    public String getUsername()
    {
        return m_username;
    }

    public void setUsername(String username)
    {
        m_username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public LocalDate getBirthDate()
    {
        return m_birthDate;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        m_birthDate = birthDate;
    }
}