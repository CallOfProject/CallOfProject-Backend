package callofproject.dev.authentication.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class RegisterRequest
{
    @JsonProperty("first_name")
    @NotBlank(message = "first name cannot be empty")
    @NotEmpty(message = "first name cannot be empty")
    private String first_name;
    @JsonProperty("last_name")
    @NotBlank(message = "last name cannot be empty")
    @NotEmpty(message = "last name cannot be empty")
    private String last_name;
    @JsonProperty("middle_name")
    private String middle_name;
    @JsonProperty("username")
    @NotBlank(message = "username cannot be empty")
    @NotEmpty(message = "username cannot be empty")
    private String username;
    @NotBlank(message = "email cannot be empty")
    @NotEmpty(message = "email cannot be empty")
    @Pattern(regexp = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", message = "invalid email format")
    private String email;
    @NotBlank(message = "password cannot be empty")
    @NotEmpty(message = "password cannot be empty")
    private String password;
    @NotNull(message = "birth date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "date format: dd/MM/yyyy", type = "string")
    private LocalDate birth_date;


    public RegisterRequest()
    {
    }

    public RegisterRequest(String firstName, String lastName, String middleName, String username, String email, String password, LocalDate birthDate)
    {
        first_name = firstName;
        last_name = lastName;
        middle_name = middleName;
        this.username = username;
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
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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