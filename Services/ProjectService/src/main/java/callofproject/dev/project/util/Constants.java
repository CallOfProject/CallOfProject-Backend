package callofproject.dev.project.util;

/**
 * Class containing constants for package names and file paths used in the project.
 */
public final class Constants
{
    /**
     * Prevents instantiation of this utility class.
     */
    private Constants()
    {
    }

    /**
     * The package name for repositories in the project.
     */
    public static final String REPO_PACKAGE = "callofproject.dev.repository.project";

    /**
     * The base package name for services in the project.
     */
    public static final String SERVICE_BASE_PACKAGE = "callofproject.dev.project";

    /**
     * The classpath location of the application properties file used for testing.
     */
    public static final String TEST_PROPERTIES_FILE = "classpath:application-test.properties";

    /**
     * The file path for the test database, typically used for integration tests.
     */
    public static final String TEST_DB_PATH = "test_data/callofproject_project_test_db.mv.db";

    /**
     * The package name for service classes in the project.
     */
    public static final String PROJECT_SERVICE = "callofproject.dev.project.service";

    /**
     * The package name for the Data Access Layer (DAL) classes in the project.
     */
    public static final String PROJECT_DAL = "callofproject.dev.repository.project.dal";

    /**
     * The package name for repository interfaces in the project.
     */
    public static final String PROJECT_REPOSITORY = "callofproject.dev.repository.project.repository";

    /**
     * The package name for entity classes in the project.
     */
    public static final String PROJECT_ENTITY = "callofproject.dev.repository.project.entity";

    /**
     * The package name for utility classes in the project.
     */
    public static final String PROJECT_UTIL = "callofproject.dev.project.util";
}
