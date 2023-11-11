package callofproject.dev.authentication.dto;

import callofproject.dev.repository.authentication.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.time.LocalDate;
import java.util.Set;

public record UserShowingAdminDTO(String username,
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
                                  @JsonProperty("birth_date")
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                  LocalDate birthDate)
{
}