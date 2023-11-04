package callofproject.dev.library.constant.exception;

public class CopServiceException extends Exception
{
    private final int m_statusCode;
    public CopServiceException(String message, int statusCode)
    {
        super(message);
        m_statusCode = statusCode;
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }

    public int getStatusCode()
    {
        return m_statusCode;
    }
}
