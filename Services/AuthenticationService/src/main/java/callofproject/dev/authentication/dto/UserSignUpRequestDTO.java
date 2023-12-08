package callofproject.dev.authentication.dto;

import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public class UserSignUpRequestDTO
{
    @NotBlank(message = "email cannot be empty")
    @NotEmpty
    private String email;
    @NotBlank(message = "first name cannot be empty")
    @NotEmpty
    @JsonProperty("first_name")
    private String firstName;
    @NotBlank(message = "middle name cannot be empty")
    @NotEmpty
    @JsonProperty("middle_name")
    private String middleName;
    @NotBlank(message = "last name cannot be empty")
    @NotEmpty
    @JsonProperty("last_name")
    private String lastName;
    @NotBlank(message = "username cannot be empty")
    @NotEmpty
    private String username;
    @NotBlank(message = "password cannot be empty")
    @NotEmpty
    private String password;

    @NotBlank(message = "birthdate cannot be empty")
    @NotEmpty
    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    private LocalDate birthDate;

    private RoleEnum role;

    public UserSignUpRequestDTO()
    {
    }

    public UserSignUpRequestDTO(String email, String firstName, String middleName, String lastName,
                                String username, String password, LocalDate birthDate,
                                RoleEnum role)
    {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.role = role;
    }

    public UserSignUpRequestDTO(String email, String firstName, String middleName, String lastName,
                                String username, String password, LocalDate birthDate)
    {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        role = RoleEnum.ROLE_USER;
    }

    public RoleEnum getRole()
    {
        return role;
    }

    public void setRole(RoleEnum role)
    {
        this.role = role;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }
}

