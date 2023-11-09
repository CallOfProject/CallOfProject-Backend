package callofproject.dev.repository.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserResponseDTO<T>
{

    private final boolean m_success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String m_token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String m_refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T m_object;

    public UserResponseDTO(boolean success, String token, String refreshToken)
    {
        m_success = success;
        m_token = token;
        m_refreshToken = refreshToken;
        m_object = null;
    }

    public UserResponseDTO(boolean success)
    {
        m_success = success;
        m_token = null;
        m_refreshToken = null;
    }

    public UserResponseDTO(boolean success, T object)
    {
        m_success = success;
        m_token = null;
        m_refreshToken = null;
        m_object = object;
    }

    public boolean isSuccess()
    {
        return m_success;
    }

    public String getToken()
    {
        return m_token;
    }

    public String getRefreshToken()
    {
        return m_refreshToken;
    }

    public T getObject()
    {
        return m_object;
    }
}
