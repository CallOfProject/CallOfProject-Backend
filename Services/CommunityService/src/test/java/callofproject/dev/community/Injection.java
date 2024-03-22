package callofproject.dev.community;

import callofproject.dev.community.service.ConnectionServiceCallback;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@Component
@SpringBootTest
public class Injection
{
    @Autowired
    private ConnectionServiceCallback m_connectionService;

    @Autowired
    private CommunityServiceHelper m_communityServiceHelper;

    @Autowired
    private IUserRepository userRepository;

    public IUserRepository getUserRepository()
    {
        return userRepository;
    }

    public ConnectionServiceCallback getConnectionService()
    {
        return m_connectionService;
    }

    public CommunityServiceHelper getCommunityServiceHelper()
    {
        return m_communityServiceHelper;
    }
}
