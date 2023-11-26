package callofproject.dev.nosql;

/**
 * NoSqlBeanName class represent the bean name of the NoSQLRepository.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
public final class NoSqlBeanName
{
    // Prevent instantiation
    private NoSqlBeanName()
    {
    }

    /**
     * NoSqlRepository bean name
     */
    public static final String NO_SQL_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql";

    /**
     * MatchRepository bean name
     */
    public static final String MATCH_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql.repository.IMatchDbRepository";

    /**
     * ProjectTagRepository bean name
     */
    public static final String PROJECT_TAG_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql.repository.IProjectTagRepository";

    /**
     * UserTagRepository bean name
     */
    public static final String USER_TAG_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql.repository.IUserTagRepository";

    /**
     * MatchServiceHelper bean name
     */
    public static final String MATCH_SERVICE_HELPER_BEAN_NAME = "callofproject.dev.nosql.dal.MatchServiceHelper";

    /**
     * UserTagServiceHelper bean name
     */
    public static final String USER_TAG_SERVICE_HELPER_BEAN_NAME = "callofproject.dev.nosql.dal.UserTagServiceHelper";

    /**
     * ProjectTagServiceHelper bean name
     */
    public static final String PROJECT_TAG_SERVICE_HELPER_BEAN_NAME = "callofproject.dev.nosql.dal.ProjectTagServiceHelper";
}
