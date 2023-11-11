package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleMessageResponseDTO<T>
{
    @JsonProperty("page")
    private final int m_pageCount;
    @JsonProperty("item_count")
    private final int m_itemCount;
    private final String m_message;
    @JsonProperty("status_code")
    private final int m_statusCode;
    private final T m_objects;

    public MultipleMessageResponseDTO(int pageCount, int itemCount, String message, int statusCode, T object)
    {
        m_pageCount = pageCount;
        m_itemCount = itemCount;
        m_message = message;
        m_statusCode = statusCode;
        m_objects = object;
    }

    public String getMessage()
    {
        return m_message;
    }

    public int getStatusCode()
    {
        return m_statusCode;
    }

    public T getObjects()
    {
        return m_objects;
    }

    public int getPageCount()
    {
        return m_pageCount;
    }

    public int getItemCount()
    {
        return m_itemCount;
    }
}
