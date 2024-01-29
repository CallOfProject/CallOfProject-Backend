package callofproject.dev.task;

import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.task", "callofproject.dev.data.task"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.task", "callofproject.dev.data.task"})
@EntityScan(basePackages = "callofproject.dev.data.task")
public class TaskServiceApplication implements CommandLineRunner
{
    private final TaskServiceHelper m_taskServiceHelper;

    public TaskServiceApplication(TaskServiceHelper taskServiceHelper)
    {
        m_taskServiceHelper = taskServiceHelper;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
  /*      var task = m_taskServiceHelper.findTaskById(UUID.fromString("5f1efc28-6a5e-4fcd-9e9a-72fcbe1b2724"));
        var user = m_taskServiceHelper.findUserByUsername("deneme");
        task.get().getAssignees().stream().map(User::getUsername).forEach(System.out::println);
        user.get().getAssignedTasks().stream().map(Task::getTaskId).forEach(System.out::println);*/
    }
}
