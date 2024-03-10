package callofproject.dev.data.common.clas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessageArray<T>
{
    @JsonProperty("message")
    private String message;
    @JsonProperty("status_code")
    private int statusCode;
    @JsonProperty("object")
    private T[] object;

    @SafeVarargs
    public ResponseMessageArray(String message, int statusCode, T...object)
    {
        this.message = message;
        this.statusCode = statusCode;
        this.object = object;
    }

    public String getMessage()
    {
        return message;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public T[] getObject()
    {
        return object;
    }

    public void setObject(T[] object)
    {
        this.object = object;
    }
}