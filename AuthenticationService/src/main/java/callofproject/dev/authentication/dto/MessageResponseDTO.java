package callofproject.dev.authentication.dto;

public class MessageResponseDTO<T>
{
    private final String m_message;
    private final int m_statusCode;
    private final T m_object;

    public MessageResponseDTO(String message, int statusCode, T object)
    {
        m_message = message;
        m_statusCode = statusCode;
        m_object = object;
    }

    public String getMessage()
    {
        return m_message;
    }

    public int getStatusCode()
    {
        return m_statusCode;
    }

    public T getObject()
    {
        return m_object;
    }
}
