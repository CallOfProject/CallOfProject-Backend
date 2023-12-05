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
    private String first_name;
    @JsonProperty("last_name")
    @NotBlank(message = "last name cannot be empty")
    @NotEmpty
    private String last_name;
    @JsonProperty("middle_name")
    @NotBlank(message = "middle name cannot be empty")
    @NotEmpty
    private String middle_name;
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
    private LocalDate birth_date;


    public RegisterRequest()
    {
    }

    public RegisterRequest(String firstName, String lastName, String middleName, String username, String email, String password, LocalDate birthDate)
    {
        first_name = firstName;
        last_name = lastName;
        middle_name = middleName;
        m_username = username;
        this.email = email;
        this.password = password;
        birth_date = birthDate;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getMiddle_name()
    {
        return middle_name;
    }

    public void setMiddle_name(String middle_name)
    {
        this.middle_name = middle_name;
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

    public LocalDate getBirth_date()
    {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date)
    {
        this.birth_date = birth_date;
    }
}