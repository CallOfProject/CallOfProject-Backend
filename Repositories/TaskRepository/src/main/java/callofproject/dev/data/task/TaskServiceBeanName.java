package callofproject.dev.data.task;

public final class TaskServiceBeanName
{

    private TaskServiceBeanName()
    {
    }

    public static final String BASE_BEAN_NAME = "callofproject.dev.data.task";
    public static final String REPOSITORY_BEAN_NAME = BASE_BEAN_NAME + ".repository";
    public static final String SERVICE_BEAN_NAME = BASE_BEAN_NAME + ".service";
    public static final String ENTITY_BEAN_NAME = BASE_BEAN_NAME + ".entity";
    public static final String ROLE_REPOSITORY_BEAN_NAME = BASE_BEAN_NAME + ".repository.IRoleRepository";
    public static final String PROJECT_REPOSITORY_BEAN_NAME = BASE_BEAN_NAME + ".repository.IProjectRepository";
    public static final String PROJECT_PARTICIPANT_REPOSITORY_BEAN_NAME = BASE_BEAN_NAME + ".repository.IProjectParticipantRepository";


}
