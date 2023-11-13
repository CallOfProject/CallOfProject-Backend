package callofproject.dev.authentication.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public class RegisterRequest
{
    @JsonProperty("first_name")
    @NotBlank(message = "first name cannot be empty")
    @NotEmpty
    private String m_firstName;
    @JsonProperty("last_name")
    @NotBlank(message = "last name cannot be empty")
    @NotEmpty
    private String m_lastName;
    @JsonProperty("middle_name")
    @NotBlank(message = "middle name cannot be empty")
    @NotEmpty
    private String m_middleName;
    @JsonProperty("username")
    @NotBlank(message = "username cannot be empty")
    @NotEmpty
    private String m_username;
    @NotBlank(message = "email cannot be empty")
    @NotEmpty
    private String email;
    @NotBlank(message = "password cannot be empty")
    @NotEmpty
    private String password;
    @NotBlank(message = "birthdate cannot be empty")
    @NotEmpty
    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate m_birthDate;


    public RegisterRequest()
    {
    }

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