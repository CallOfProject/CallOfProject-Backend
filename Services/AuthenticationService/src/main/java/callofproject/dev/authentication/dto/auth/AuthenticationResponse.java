package callofproject.dev.authentication.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


/**
 * Data Transfer Object for authentication.
 */
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


    /**
     * Constructor
     *
     * @param accessToken  The access token.
     * @param refreshToken The refresh token.
     * @param isSuccess    The success.
     * @param role         The role.
     * @param isBlocked    The is blocked.
     * @param userId       The user id.
     */
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


    /**
     * Constructor
     *
     * @param accessToken  The access token.
     * @param refreshToken The refresh token.
     * @param isSuccess    The success.
     * @param role         The role.
     * @param userId       The user id.
     */
    public AuthenticationResponse(String accessToken, String refreshToken, boolean isSuccess, String role, UUID userId)
    {
        user_id = userId;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isSuccess = isSuccess;
    }


    /**
     * Constructor
     *
     * @param isSuccess The success.
     * @param isBlocked The is blocked.
     */
    public AuthenticationResponse(boolean isSuccess, boolean isBlocked)
    {
        this.isSuccess = isSuccess;
        this.isBlocked = isBlocked;
    }


    /**
     * isBlocked
     *
     * @return isBlocked
     */
    public boolean isBlocked()
    {
        return isBlocked;
    }


    /**
     * setBlocked
     *
     * @param blocked blocked
     */
    public void setBlocked(boolean blocked)
    {
        isBlocked = blocked;
    }


    /**
     * getRole
     *
     * @return role
     */
    public String getRole()
    {
        return role;
    }


    /**
     * setRole
     *
     * @param role role
     */
    public void setRole(String role)
    {
        this.role = role;
    }


    /**
     * getUser_id
     *
     * @return user_id
     */
    public UUID getUser_id()
    {
        return user_id;
    }


    /**
     * setUser_id
     *
     * @param user_id user_id
     */
    public void setUser_id(UUID user_id)
    {
        this.user_id = user_id;
    }


    /**
     * isSuccess
     *
     * @return isSuccess
     */
    public boolean isSuccess()
    {
        return isSuccess;
    }


    /**
     * setSuccess
     *
     * @param success success
     */
    public void setSuccess(boolean success)
    {
        isSuccess = success;
    }


    /**
     * getAccessToken
     *
     * @return accessToken
     */
    public String getAccessToken()
    {
        return accessToken;
    }


    /**
     * setAccessToken
     *
     * @param accessToken accessToken
     */
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }


    /**
     * getRefreshToken
     *
     * @return refreshToken
     */
    public String getRefreshToken()
    {
        return refreshToken;
    }


    /**
     * setRefreshToken
     *
     * @param refreshToken refreshToken
     */
    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }
}