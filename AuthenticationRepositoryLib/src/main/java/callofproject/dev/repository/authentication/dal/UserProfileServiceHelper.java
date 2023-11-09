package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.repository.rdbms.IUserProfileRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.USER_PROFILE_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.USER_PROFILE_REPOSITORY_BEAN;

@Component(USER_PROFILE_DAL_BEAN)
@Lazy
public class UserProfileServiceHelper
{
    private final IUserProfileRepository m_userProfileRepository;

    public UserProfileServiceHelper(@Qualifier(USER_PROFILE_REPOSITORY_BEAN) IUserProfileRepository userProfileRepository)
    {
        m_userProfileRepository = userProfileRepository;
    }

    public UserProfile saveUserProfile(UserProfile userProfile)
    {
        return m_userProfileRepository.save(userProfile);
    }

    public void removeUserProfile(UserProfile userProfile)
    {
        m_userProfileRepository.delete(userProfile);
    }

    public void removeUserProfileById(UUID uuid)
    {
        m_userProfileRepository.deleteById(uuid);
    }

    public Optional<UserProfile> findByIdUserProfile(UUID id)
    {
        return m_userProfileRepository.findById(id);
    }

    public Iterable<UserProfile> findAllUserProfile()
    {
        return m_userProfileRepository.findAll();
    }
}
