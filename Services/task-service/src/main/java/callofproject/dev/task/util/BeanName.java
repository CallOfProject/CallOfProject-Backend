package callofproject.dev.task.util;

/**
 * BeanName class
 * <p>
 * This class contains the bean names for the service and repository beans.
 */
public final class BeanName
{
    private BeanName()
    {
    }

    /**
     * The name of the service bean.
     */
    public static final String SERVICE_BEAN_NAME = "callofproject.dev.task";
    /**
     * The name of the repository bean.
     */
    public static final String REPOSITORY_BEAN_NAME = "callofproject.dev.data.task";

    /**
     * The name of the test properties file.
     */
    public static final String TEST_PROPERTIES_FILE = "classpath:application-test.properties";
}
