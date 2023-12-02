package callofproject.dev.data.common.clas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage<T>
{
    @JsonProperty("message")
    private final String m_message;
    @JsonProperty("status_code")
    private final int m_statusCode;
    @JsonProperty("object")
    private final T m_object;

    public ResponseMessage(String message, int statusCode, T object)
    {
        m_message = message;
        m_statusCode = statusCode;
        m_object = object;
    }

    public String getMessage()
    {
        return m_message;
    }


    public T getObject()
    {
        return m_object;
    }
}