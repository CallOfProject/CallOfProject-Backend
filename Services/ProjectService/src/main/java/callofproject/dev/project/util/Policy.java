package callofproject.dev.project.util;

public final class Policy
{
    private Policy()
    {
    }


    /**
     * Maximum number of projects the user can create. If not completed.
     */
    public static final int OWNER_MAX_PROJECT_COUNT = 5;

    /**
     * Maximum number of projects that the user can participate in (excluding own project)
     */
    public static final int MAX_PARTICIPANT_PROJECT_COUNT = 5;

    /**
     * Maximum project count per user
     */
    public static final int MAX_PROJECT_COUNT = OWNER_MAX_PROJECT_COUNT + MAX_PARTICIPANT_PROJECT_COUNT;

}
