package callofproject.dev.data.common.clas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleResponseMessage<T>
{
    @JsonProperty("item_count")
    private final int m_itemCount;
    @JsonProperty("message")
    private final String m_message;
    @JsonProperty("object")
    private final T m_object;

    public MultipleResponseMessage(int itemCount, String message, T object)
    {
        m_itemCount = itemCount;
        m_message = message;
        m_object = object;
    }

    public int getItemCount()
    {
        return m_itemCount;
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
