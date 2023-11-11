package callofproject.dev.repository.authentication.enumeration;

public enum RoleEnum
{
    ROLE_USER("ROLE_USER"),
    ROLE_ROOT("ROLE_ROOT"),
    ROLE_ADMIN("ROLE_ADMIN");
    private final String role;

    RoleEnum(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }
}
