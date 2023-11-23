package callofproject.dev.nosql;

public final class NoSqlBeanName
{
    private NoSqlBeanName()
    {
    }
    public static final String NO_SQL_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql";
    public static final String MATCH_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql.repository.IMatchDbRepository";
    public static final String PROJECT_TAG_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql.repository.IProjectTagRepository";
    public static final String USER_TAG_REPOSITORY_BEAN_NAME = "callofproject.dev.nosql.repository.IUserTagRepository";
    public static final String MATCH_SERVICE_HELPER_BEAN_NAME = "callofproject.dev.nosql.dal.MatchServiceHelper";
    public static final String USER_TAG_SERVICE_HELPER_BEAN_NAME = "callofproject.dev.nosql.dal.UserTagServiceHelper";
    public static final String PROJECT_TAG_SERVICE_HELPER_BEAN_NAME = "callofproject.dev.nosql.dal.ProjectTagServiceHelper";
}
