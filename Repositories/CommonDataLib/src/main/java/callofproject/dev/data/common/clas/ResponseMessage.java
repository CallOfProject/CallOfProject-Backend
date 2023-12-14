package callofproject.dev.data.common.clas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage<T>
{
    @JsonProperty("message")
    private final String message;
    @JsonProperty("status_code")
    private final int statusCode;
    @JsonProperty("object")
    private final T object;

    public ResponseMessage(String message, int statusCode, T object)
    {
        this.message = message;
        this.statusCode = statusCode;
        this.object = object;
    }

    public String getMessage()
    {
        return message;
    }


    public T getObject()
    {
        return object;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}