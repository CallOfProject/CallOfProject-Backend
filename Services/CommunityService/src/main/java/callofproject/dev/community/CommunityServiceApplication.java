package callofproject.dev.community;

import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EntityScan(basePackages = "callofproject.dev.data.community.entity")
public class CommunityServiceApplication implements CommandLineRunner
{
    private final IUserRepository m_userRepository;

    public CommunityServiceApplication(IUserRepository userRepository)
    {
        m_userRepository = userRepository;
    }


    public static void main(String[] args)
    {
        SpringApplication.run(CommunityServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        var nuri = m_userRepository.findByUsername("cop_root");
        var elif = m_userRepository.findByUsername("burak");


    }
}
