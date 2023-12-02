package callofproject.dev.nosql.enums;

/**
 * NotificationType class represent the enum of the NotificationType entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
public enum NotificationType
{
    INFORMATION("Information"),
    WARNING("Warning"),
    ERROR("Error"),
    SUCCESS("Success");

    private final String m_value;

    NotificationType(String value)
    {
        m_value = value;
    }

    public String getValue()
    {
        return m_value;
    }
}
