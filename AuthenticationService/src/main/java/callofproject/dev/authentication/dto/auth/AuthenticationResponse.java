package callofproject.dev.authentication.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse
{

    private String role;
    @JsonProperty("success")
    private boolean isSuccess;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public AuthenticationResponse(String accessToken, String refreshToken, boolean isSuccess, String role)
    {
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isSuccess = isSuccess;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
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