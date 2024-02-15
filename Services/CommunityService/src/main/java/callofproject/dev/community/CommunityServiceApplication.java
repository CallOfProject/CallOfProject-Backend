package callofproject.dev.community;

import callofproject.dev.data.community.entity.BlockedConnections;
import callofproject.dev.data.community.entity.UserConnection;
import callofproject.dev.data.community.repository.IBlockedConnectionsRepository;
import callofproject.dev.data.community.repository.IUserConnectionRepository;
import callofproject.dev.data.community.repository.IUserRepository;
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
@ComponentScan(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EntityScan(basePackages = "callofproject.dev.data.community.entity")
public class CommunityServiceApplication implements CommandLineRunner
{
    private final IUserRepository m_userRepository;
    private final IUserConnectionRepository m_userConnectionRepository;
    private final IBlockedConnectionsRepository m_blockedConnections;

    public CommunityServiceApplication(IUserRepository userRepository, IUserConnectionRepository userConnectionRepository, IBlockedConnectionsRepository blockedConnections)
    {
        m_userRepository = userRepository;
        m_userConnectionRepository = userConnectionRepository;
        m_blockedConnections = blockedConnections;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(CommunityServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        var nuri = m_userRepository.findByUsername("cop_root");
        var elif = m_userRepository.findByUsername("cop_admin");


        var l = new BlockedConnections(nuri.get(), elif.get());
        m_blockedConnections.save(l);
        nuri.get().addBlockedUser(l);
        var con = m_userConnectionRepository.findById(UUID
                .fromString("fb3c68ab-fe66-48f3-a583-5f6fbeba6ffc")).get();
        nuri.get().removeConnection(con);

        m_userConnectionRepository.delete(con);

        //nuri.get().removeConnection(elif.get());


        //var k = m_userConnectionRepository.save(new UserConnection(nuri.get(), elif.get()));
        //nuri.get().addConnection(k);

        //m_userRepository.save(nuri.get());


        /*nuri.get().getConnections().stream().map(UserConnection::getConnection).map(User::getUsername).forEach(System.out::println);
        elif.get().getConnections().stream().map(UserConnection::getConnection).map(User::getUsername).forEach(System.out::println);*/
        //elif.get().addConnection(new UserConnection(nuri.get(),elif.get()));

       /* var connection = new UserConnection(nuri.get(), elif.get());
        m_userConnectionRepository.save(connection);*/

        //m_userRepository.save(nuri.get());
        //elif.get().blockUser(nuri.get());
        //m_userRepository.save(elif.get());

        /*var connection = new Connections();
        connection.addUser(elif.get());
        nuri.get().addFriend(connection);

        m_connectionsRepository.save(connection);
        m_userRepository.save(nuri.get());*/
    }
}
