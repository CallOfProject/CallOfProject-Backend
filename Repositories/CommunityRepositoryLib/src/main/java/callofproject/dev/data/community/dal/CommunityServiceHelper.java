package callofproject.dev.data.community.dal;

import callofproject.dev.data.community.entity.User;
import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class CommunityServiceHelper
{
    private final IUserRepository m_userRepository;

    public CommunityServiceHelper(IUserRepository userRepository)
    {
        m_userRepository = userRepository;
    }

    public void saveUser(User user)
    {
        m_userRepository.save(user);
    }

}
