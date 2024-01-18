package callofproject.dev.authentication.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;


/**
 * Data Transfer Object for a register request.
 */
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


    /**
     * Constructor.
     */
    public RegisterRequest()
    {
    }


    /**
     * Constructor.
     *
     * @param firstName  The first name.
     * @param lastName   The last name.
     * @param middleName The middle name.
     * @param username   The username.
     * @param email      The email.
     * @param password   The password.
     * @param birthDate  The birth date.
     */
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


    /**
     * Gets the first name.
     *
     * @return The first name.
     */
    public String getFirst_name()
    {
        return first_name;
    }


    /**
     * Sets the first name.
     *
     * @param first_name The first name.
     */
    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }


    /**
     * Gets the last name.
     *
     * @return The last name.
     */
    public String getLast_name()
    {
        return last_name;
    }


    /**
     * Sets the last name.
     *
     * @param last_name The last name.
     */
    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }


    /**
     * Gets the middle name.
     *
     * @return The middle name.
     */
    public String getMiddle_name()
    {
        return middle_name;
    }


    /**
     * Sets the middle name.
     *
     * @param middle_name The middle name.
     */
    public void setMiddle_name(String middle_name)
    {
        this.middle_name = middle_name;
    }


    /**
     * Gets the username.
     *
     * @return The username.
     */
    public String getUsername()
    {
        return username;
    }


    /**
     * Sets the username.
     *
     * @param username The username.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }


    /**
     * Gets the email.
     *
     * @return The email.
     */
    public String getEmail()
    {
        return email;
    }


    /**
     * Sets the email.
     *
     * @param email The email.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }


    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword()
    {
        return password;
    }


    /**
     * Sets the password.
     *
     * @param password The password.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }


    /**
     * Gets the birth date.
     *
     * @return The birth date.
     */
    public LocalDate getBirth_date()
    {
        return birth_date;
    }


    /**
     * Sets the birth date.
     *
     * @param birth_date The birth date.
     */
    public void setBirth_date(LocalDate birth_date)
    {
        this.birth_date = birth_date;
    }
}