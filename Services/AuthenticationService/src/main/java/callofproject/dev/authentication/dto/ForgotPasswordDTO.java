package callofproject.dev.authentication.dto;

/**
 * Data Transfer Object for a user.
 */
public record ForgotPasswordDTO(String new_password, String user_token)
{
}
