package callofproject.dev.authentication.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record UserUpdateDTOAdmin(

        @JsonProperty("admin_id")
        String adminId,
        @NotBlank(message = "username name cannot be empty")
        @NotEmpty
        String username,
        @JsonProperty("first_name")
        @NotBlank(message = "first name cannot be empty")
        @NotEmpty
        String firstName,
        @JsonProperty("last_name")
        @NotBlank(message = "last name cannot be empty")
        @NotEmpty
        String lastName,
        @JsonProperty("middle_name")
        @NotBlank(message = "middle name cannot be empty")
        @NotEmpty
        String middleName,

        @NotBlank(message = "email cannot be empty")
        @NotEmpty
        String email,

        @JsonProperty("is_account_blocked")
        @NotBlank(message = "isBlocked cannot be empty")
        @NotEmpty
        boolean isAccountBlocked,

        @NotBlank(message = "birthdate cannot be empty")
        @NotEmpty
        @JsonProperty("birth_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate birthDate
)
{
        @Override
        public String toString()
        {
                return "UserUpdateDTOAdmin{" +
                        "firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", middleName='" + middleName + '\'' +
                        ", email='" + email + '\'' +
                        ", isAccountBlocked=" + isAccountBlocked +
                        ", birthDate=" + birthDate +
                        '}';
        }
}
