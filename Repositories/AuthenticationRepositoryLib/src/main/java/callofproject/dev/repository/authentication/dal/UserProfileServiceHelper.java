package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.repository.rdbms.IUserProfileRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
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
        return doForRepository(() -> m_userProfileRepository.save(userProfile), "UserProfileRepository::saveUserProfile");
    }

    public void removeUserProfile(UserProfile userProfile)
    {
        doForRepository(() -> m_userProfileRepository.delete(userProfile), "UserProfileRepository::removeUserProfile");
    }

    public void removeUserProfileById(UUID uuid)
    {
        doForRepository(() -> m_userProfileRepository.deleteById(uuid), "UserProfileRepository::removeUserProfileById");
    }

    public Optional<UserProfile> findByIdUserProfile(UUID id)
    {
        return doForRepository(() -> m_userProfileRepository.findById(id), "UserProfileRepository::findByIdUserProfile");
    }

    public Iterable<UserProfile> findAllUserProfile()
    {
        return doForRepository(m_userProfileRepository::findAll, "UserProfileRepository::findAllUserProfile");
    }

    public Optional<UserProfile> findUserProfileByUserId(UUID userId)
    {
        return doForRepository(() -> m_userProfileRepository.findUserProfileByUserId(userId), "UserProfileRepository::findUserProfileByUserId");
    }

    public Optional<UserProfile> findUserProfileByUsername(String username)
    {
        return doForRepository(() -> m_userProfileRepository.findUserProfileByUsername(username), "UserProfileRepository::findByUsername");
    }
}
