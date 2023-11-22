package callofproject.dev.authentication.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AuthenticationResponse
{

    private UUID user_id;
    private String role;
    @JsonProperty("success")
    private boolean isSuccess;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("is_blocked")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean isBlocked;


    public AuthenticationResponse(String accessToken, String refreshToken, boolean isSuccess,
                                  String role, boolean isBlocked, UUID userId)
    {
        user_id = userId;
        this.isBlocked = isBlocked;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isSuccess = isSuccess;
    }

    public AuthenticationResponse(String accessToken, String refreshToken, boolean isSuccess, String role, UUID userId)
    {
        user_id = userId;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isSuccess = isSuccess;
    }

    public AuthenticationResponse(boolean isSuccess, boolean isBlocked)
    {
        this.isSuccess = isSuccess;
        this.isBlocked = isBlocked;
    }

    public boolean isBlocked()
    {
        return isBlocked;
    }

    public void setBlocked(boolean blocked)
    {
        isBlocked = blocked;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public UUID getUser_id()
    {
        return user_id;
    }

    public void setUser_id(UUID user_id)
    {
        this.user_id = user_id;
    }

    public boolean isSuccess()
    {
        return isSuccess;
    }

    public void setSuccess(boolean success)
    {
        isSuccess = success;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }
}