package callofproject.dev.data.common.enums;

public enum NotificationType {
    INFORMATION("Information"),
    WARNING("Warning"),
    ERROR("Error"),
    SUCCESS("Success");

    private final String m_value;

    private NotificationType(String value) {
        this.m_value = value;
    }

    public String getValue() {
        return this.m_value;
    }
}