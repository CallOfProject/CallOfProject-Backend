package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data Transfer Object for a user response.
 * It is used to transfer data between the client and the server.
 *
 * @param <T> the type of the object to be transferred
 */
public class UserResponseDTO<T>
{
    private final boolean m_success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String m_token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String m_refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T m_object;


    /**
     * Constructor for the UserResponseDTO class.
     * It is used to create an instance of the class with all fields set to their default values.
     */
    public UserResponseDTO(boolean success, String token, String refreshToken)
    {
        m_success = success;
        m_token = token;
        m_refreshToken = refreshToken;
        m_object = null;
    }

    /**
     * Constructor for the UserResponseDTO class.
     * It is used to create an instance of the class with all fields set to their default values.
     */
    public UserResponseDTO(boolean success, String token, String refreshToken, T object)
    {
        m_success = success;
        m_token = token;
        m_refreshToken = refreshToken;
        m_object = null;
        m_object = object;
    }

    /**
     * Constructor for the UserResponseDTO class.
     * It is used to create an instance of the class with all fields set to their default values.
     */
    public UserResponseDTO(boolean success)
    {
        m_success = success;
        m_token = null;
        m_refreshToken = null;
    }


    /**
     * Constructor for the UserResponseDTO class.
     * It is used to create an instance of the class with all fields set to their default values.
     */
    public UserResponseDTO(boolean success, T object)
    {
        m_success = success;
        m_token = null;
        m_refreshToken = null;
        m_object = object;
    }


    /**
     * Checks if the operation was successful.
     *
     * @return True if the operation was successful, false otherwise.
     */
    public boolean isSuccess()
    {
        return m_success;
    }

    /**
     * Gets the authentication token.
     *
     * @return The authentication token.
     */
    public String getToken()
    {
        return m_token;
    }

    /**
     * Gets the refresh token.
     *
     * @return The refresh token.
     */
    public String getRefreshToken()
    {
        return m_refreshToken;
    }

    /**
     * Gets the object contained in the response message.
     *
     * @return The object contained in the response message.
     */
    public T getObject()
    {
        return m_object;
    }
}
