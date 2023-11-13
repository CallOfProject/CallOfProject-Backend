package callofproject.dev.authentication.dto;

public record UserLoginRequestDTO(String username, String password)
{
    public UserLoginRequestDTO(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
}