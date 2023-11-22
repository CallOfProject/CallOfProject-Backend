package callofproject.dev.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleMessageResponseDTO<T>
{
    @JsonProperty("total_page")
    private final long m_totalPage;
    @JsonProperty("page")
    private final int m_page;
    @JsonProperty("item_count")
    private final int m_itemCount;
    @JsonProperty("message")
    private final String m_message;
    @JsonProperty("object")
    private final T m_object;

    public MultipleMessageResponseDTO(long totalPage, int page, int itemCount, String message, T object)
    {
        m_totalPage = totalPage;
        m_page = page;
        m_itemCount = itemCount;
        m_message = message;
        m_object = object;
    }
}
