package callofproject.dev.authentication.util;

/**
 * Class containing constants for package names and file paths used in the project.
 */
public final class Util
{
    /**
     * Prevents instantiation of this utility class.
     */
    private Util()
    {
    }

    /**
     * The fully qualified name of the MapperConfig class.
     */
    public static final String MAPPER_CONFIG_BEAN = "callofproject.dev.authentication.mapper.MapperConfig";

    /**
     * The package name for repository classes.
     */
    public static final String REPO_PACKAGE = "callofproject.dev.repository.authentication";

    /**
     * The base package name for the authentication module.
     */
    public static final String BASE_PACKAGE = "callofproject.dev.authentication";

    /**
     * The file path for the test properties file.
     */
    public static final String TEST_PROPERTIES_FILE = "classpath:application-test.properties";

    /**
     * The file path for the test database.
     */
    public static final String TEST_DB_PATH = "test_data/callofproject_authentication_test_db.mv.db";

    /**
     * The package name for user management services.
     */
    public static final String USER_MANAGEMENT_SERVICE = "callofproject.dev.authentication.service.usermanagement";

    /**
     * The package name for authentication services.
     */
    public static final String AUTHENTICATION_SERVICE = "callofproject.dev.authentication.service.authentication";

}
