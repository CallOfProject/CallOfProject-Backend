package callofproject.dev.community;

/**
 * @author Nuri Can ÖZTÜRK
 * This class contains the bean names for the community service.
 */
public final class CommunityServiceBeanName
{
    /**
     * Private constructor to prevent instantiation.
     */
    private CommunityServiceBeanName()
    {

    }

    /**
     * The base package for the community service.
     */
    public static final String BASE_PACKAGE = "callofproject.dev.community";

    /**
     * The repository package for the community service.
     */
    public static final String REPOSITORY_PACKAGE = "callofproject.dev.data.community";

    /**
     * The entity package for the community service.
     */
    public static final String ENTITY_PACKAGE = "callofproject.dev.data.community.entity";

    /**
     * The service package for the community service.
     */
    public static final String TEST_PROPERTIES_FILE = "classpath:application-test.properties";
}
