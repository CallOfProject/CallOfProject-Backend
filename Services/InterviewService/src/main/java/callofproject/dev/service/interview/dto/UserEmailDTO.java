package callofproject.dev.service.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserEmailDTO(@JsonProperty("user_id") UUID userId, String email, String username)
{
}
