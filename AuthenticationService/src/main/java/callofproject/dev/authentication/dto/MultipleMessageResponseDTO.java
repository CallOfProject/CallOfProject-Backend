package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleMessageResponseDTO<T>
{
    @JsonProperty("page")
    private final int m_pageCount;
    @JsonProperty("item_count")
    private final int m_itemCount;
    @JsonProperty("message")
    private final String m_message;
    @JsonProperty("status_code")
    private final int m_statusCode;
    @JsonProperty("objects")
    private final T m_objects;

    public MultipleMessageResponseDTO(int pageCount, int itemCount, String message, int statusCode, T object)
    {
        m_pageCount = pageCount;
        m_itemCount = itemCount;
        m_message = message;
        m_statusCode = statusCode;
        m_objects = object;
    }
}
