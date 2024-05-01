package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.BeanName;
import callofproject.dev.repository.authentication.entity.UserTag;
import callofproject.dev.repository.authentication.repository.rdbms.IUserTagRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component(BeanName.USER_TAG_DAL_BEAN)
@Lazy
public class UserTagServiceHelper
{
    private final IUserTagRepository m_userTagRepository;

    public UserTagServiceHelper(IUserTagRepository userTagRepository)
    {
        m_userTagRepository = userTagRepository;
    }

    public Optional<UserTag> findByTagName(String tagName)
    {
        return m_userTagRepository.findByTagName(tagName);
    }

    public Optional<UserTag> findByTagId(UUID tagId)
    {
        return m_userTagRepository.findByTagId(tagId);
    }

    public UserTag saveUserTag(UserTag userTag)
    {
        return m_userTagRepository.save(userTag);
    }

    public void removeUserTag(UserTag userTag)
    {
        m_userTagRepository.delete(userTag);
    }

    public void removeUserTagById(UUID uuid)
    {
        m_userTagRepository.deleteById(uuid);
    }

    public Iterable<UserTag> findAllByIds(Iterable<UUID> ids)
    {
        return m_userTagRepository.findAllById(ids);
    }

    public Iterable<UserTag> saveAll(Iterable<UserTag> userTags)
    {
        return m_userTagRepository.saveAll(userTags);
    }
}
