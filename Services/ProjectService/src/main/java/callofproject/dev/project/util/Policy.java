package callofproject.dev.project.util;

/**
 * Utility class for policy constants.
 */
public final class Policy
{
    /**
     * Prevents instantiation of this utility class.
     */
    private Policy()
    {
    }


    /**
     * Maximum number of projects the user can create. If not completed.
     */
    public static final int OWNER_MAX_PROJECT_COUNT = 10;

    /**
     * Maximum number of projects that the user can participate in (excluding own project)
     */
    public static final int MAX_PARTICIPANT_PROJECT_COUNT = 10;

    /**
     * Maximum project count per user
     */
    public static final int MAX_PROJECT_COUNT = OWNER_MAX_PROJECT_COUNT + MAX_PARTICIPANT_PROJECT_COUNT;

}
