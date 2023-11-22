/*----------------------------------------------------------------
	FILE		: UserTagServiceHelper.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	UserTagServiceHelper class represent the helper class of the UserTagService.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.dal;

import callofproject.dev.nosql.entity.UserTag;
import callofproject.dev.nosql.repository.IUserTagRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
@SuppressWarnings("all")
public class UserTagServiceHelper
{
    private final IUserTagRepository m_userTagRepository;

    public UserTagServiceHelper(IUserTagRepository userTagRepository)
    {
        m_userTagRepository = userTagRepository;
    }

    public UserTag saveUserTag(UserTag userTag)
    {
        return doForRepository(() -> m_userTagRepository.save(userTag), "ProjectTagServiceHelper::saveUserTag");
    }

    public void removeUserTag(UserTag userTag)
    {
        doForRepository(() -> m_userTagRepository.delete(userTag), "ProjectTagServiceHelper::removeUserTag");
    }

    public void removeUserTagById(Long id)
    {
        doForRepository(() -> m_userTagRepository.deleteById(id), "ProjectTagServiceHelper::removeUserTagById");
    }

    public long count()
    {
        return doForRepository(() -> m_userTagRepository.count(), "ProjectTagServiceHelper::count");
    }

    public Iterable<UserTag> saveAll(Iterable<UserTag> userTags)
    {
        return doForRepository(() -> m_userTagRepository.saveAll(userTags), "ProjectTagServiceHelper::saveAll");
    }

    public Iterable<UserTag> getAllUserTag()
    {
        return doForRepository(() -> m_userTagRepository.findAll(), "ProjectTagServiceHelper::getAllUserTag");
    }

    public Iterable<UserTag> getAllUserTagByUserId(UUID userId)
    {
        return doForRepository(() -> m_userTagRepository.findAllByUserId(userId), "ProjectTagServiceHelper::getAllUserTagByUserId");
    }

    public Iterable<UserTag> getAllUserTagByTagName(String tagName)
    {
        return doForRepository(() -> m_userTagRepository.findAllByTagName(tagName), "ProjectTagServiceHelper::getAllUserTagByTagName");
    }

    public Iterable<UserTag> getAllUserTagByUserIdAndTagName(UUID userId, String tagName)
    {
        return doForRepository(() -> m_userTagRepository.findAllByUserIdAndTagName(userId, tagName), "ProjectTagServiceHelper::getAllUserTagByUserIdAndTagName");
    }
}
