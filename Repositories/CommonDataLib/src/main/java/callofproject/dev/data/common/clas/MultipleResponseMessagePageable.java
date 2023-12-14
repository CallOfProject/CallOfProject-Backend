package callofproject.dev.data.common.clas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleResponseMessagePageable<T>
{
    @JsonProperty("total_page")
    private final long totalPage;
    @JsonProperty("page")
    private final int page;
    @JsonProperty("item_count")
    private final int itemCount;
    @JsonProperty("message")
    private final String message;
    @JsonProperty("object")
    private final T object;

    public MultipleResponseMessagePageable(long totalPage, int page, int itemCount, String message, T object)
    {
        this.totalPage = totalPage;
        this.page = page;
        this.itemCount = itemCount;
        this.message = message;
        this.object = object;
    }

    public long getTotalPage()
    {
        return totalPage;
    }

    public int getPage()
    {
        return page;
    }

    public int getItemCount()
    {
        return itemCount;
    }

    public String getMessage()
    {
        return message;
    }

    public T getObject()
    {
        return object;
    }
}
