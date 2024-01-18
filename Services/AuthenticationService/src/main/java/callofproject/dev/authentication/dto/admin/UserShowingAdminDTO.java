package callofproject.dev.authentication.dto.admin;

import callofproject.dev.repository.authentication.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


/**
 * Data Transfer Object for a course.
 */
public record UserShowingAdminDTO(String username,

                                  @JsonProperty("user_id")
                                  UUID userId,
                                  Set<Role> roles,
                                  String email,
                                  @JsonProperty("is_account_blocked")
                                  boolean isAccountBlocked,
                                  @JsonProperty("first_name")
                                  String firstName,
                                  @JsonProperty("middle_name")
                                  String middleName,
                                  @JsonProperty("last_name")
                                  String lastName,
                                  @JsonProperty("creation_date")
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                  LocalDate creationDate,
                                  @JsonProperty("deleted_at")
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy kk:mm:ss")
                                  LocalDateTime deletedAt,
                                  @JsonProperty("birth_date")
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                  LocalDate birthDate)
{
}