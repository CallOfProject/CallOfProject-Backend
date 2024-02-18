package callofproject.dev.community;

import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EntityScan(basePackages = "callofproject.dev.data.community.entity")
public class CommunityServiceApplication implements ApplicationRunner
{
    private final CommunityServiceHelper m_communityServiceHelper;

    public CommunityServiceApplication(CommunityServiceHelper communityServiceHelper)
    {
        m_communityServiceHelper = communityServiceHelper;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(CommunityServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        /*var users = m_communityServiceHelper.findUserById(UUID.fromString("a6cbe4fe-5036-4f65-838b-0e7929f5ac2d"));

        System.out.println("--------------------");
        users.get().getConnectionRequests().stream().map(User::getUserId).forEach(System.out::println);
        System.out.println("--------------------");*/
    }
}
