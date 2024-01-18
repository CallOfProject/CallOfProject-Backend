package callofproject.dev.authentication.dto;

import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

/**
 * DTO class for the user sign up request.
 * It is used to transfer data between the client and the server.
 */
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


    /**
     * Constructor for the UserSignUpRequestDTO class.
     * It is used to create an instance of the class with all fields set to their default values.
     */
    public UserSignUpRequestDTO()
    {
    }


    /**
     * Constructor for the UserSignUpRequestDTO class.
     * It is used to create an instance of the class with all fields set to the values provided as parameters.
     *
     * @param email      The email of the user.
     * @param firstName  The first name of the user.
     * @param middleName The middle name of the user.
     * @param lastName   The last name of the user.
     * @param username   The username of the user.
     * @param password   The password of the user.
     * @param birthDate  The birth date of the user.
     * @param role       The role of the user.
     */
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


    /**
     * Constructor for the UserSignUpRequestDTO class.
     * It is used to create an instance of the class with all fields set to the values provided as parameters.
     *
     * @param email      The email of the user.
     * @param firstName  The first name of the user.
     * @param middleName The middle name of the user.
     * @param lastName   The last name of the user.
     * @param username   The username of the user.
     * @param password   The password of the user.
     * @param birthDate  The birth date of the user.
     */
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


    /**
     * Retrieves the role of the user.
     *
     * @return RoleEnum The role of the user.
     */
    public RoleEnum getRole()
    {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role of the user.
     */
    public void setRole(RoleEnum role)
    {
        this.role = role;
    }

    /**
     * Retrieves the email of the user.
     *
     * @return String The email of the user.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email of the user.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return String The first name of the user.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name of the user.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Retrieves the middle name of the user.
     *
     * @return String The middle name of the user.
     */
    public String getMiddleName()
    {
        return middleName;
    }

    /**
     * Sets the middle name of the user.
     *
     * @param middleName The middle name of the user.
     */
    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return String The last name of the user.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name of the user.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return String The username of the user.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return String The password of the user.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Retrieves the birth date of the user.
     *
     * @return LocalDate The birth date of the user.
     */
    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    /**
     * Sets the birth date of the user.
     *
     * @param birthDate The birth date of the user.
     */
    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }
}

