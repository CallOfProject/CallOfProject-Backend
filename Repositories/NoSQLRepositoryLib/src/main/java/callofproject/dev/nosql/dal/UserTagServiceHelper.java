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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.nosql.NoSqlBeanName.USER_TAG_REPOSITORY_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.USER_TAG_SERVICE_HELPER_BEAN_NAME;

@Component(USER_TAG_SERVICE_HELPER_BEAN_NAME)
@Lazy
@SuppressWarnings("all")
public class UserTagServiceHelper
{
    private final IUserTagRepository m_userTagRepository;

    public UserTagServiceHelper(@Qualifier(USER_TAG_REPOSITORY_BEAN_NAME) IUserTagRepository userTagRepository)
    {
        m_userTagRepository = userTagRepository;
    }

    /**
     * Save User Tag
     *
     * @param userTag
     * @return
     */
    public UserTag saveUserTag(UserTag userTag)
    {
        return doForRepository(() -> m_userTagRepository.save(userTag), "ProjectTagServiceHelper::saveUserTag");
    }

    /**
     * Remove user tag
     *
     * @param userTag
     */
    public void removeUserTag(UserTag userTag)
    {
        doForRepository(() -> m_userTagRepository.delete(userTag), "ProjectTagServiceHelper::removeUserTag");
    }

    /**
     * Remove user tag by id
     *
     * @param id
     */
    public void removeUserTagById(Long id)
    {
        doForRepository(() -> m_userTagRepository.deleteById(id), "ProjectTagServiceHelper::removeUserTagById");
    }

    /**
     * Find count of user tag
     *
     * @param id
     * @return UserTag
     */
    public long count()
    {
        return doForRepository(() -> m_userTagRepository.count(), "ProjectTagServiceHelper::count");
    }

    /**
     * Save all user tag
     *
     * @param id
     * @return Iterable UserTag
     */
    public Iterable<UserTag> saveAll(Iterable<UserTag> userTags)
    {
        return doForRepository(() -> m_userTagRepository.saveAll(userTags), "ProjectTagServiceHelper::saveAll");
    }

    /**
     * Get all user tag
     *
     * @param id
     * @return UserTag
     */
    public Iterable<UserTag> getAllUserTag()
    {
        return doForRepository(() -> m_userTagRepository.findAll(), "ProjectTagServiceHelper::getAllUserTag");
    }

    /**
     * Get all user tag by user id
     *
     * @param userId
     * @return Iterable user tag
     */
    public Iterable<UserTag> getAllUserTagByUserId(UUID userId)
    {
        return doForRepository(() -> m_userTagRepository.findAllByUserId(userId), "ProjectTagServiceHelper::getAllUserTagByUserId");
    }

    /**
     * Get all user tag by tag name
     *
     * @param tagName
     * @return Iterable user tag
     */
    public Iterable<UserTag> getAllUserTagByTagName(String tagName)
    {
        return doForRepository(() -> m_userTagRepository.findAllByTagName(tagName), "ProjectTagServiceHelper::getAllUserTagByTagName");
    }

    /**
     * Get all user tag by user id and tag name
     *
     * @param userId
     * @param tagName
     * @return Iterable user tag
     */
    public Iterable<UserTag> getAllUserTagByUserIdAndTagName(UUID userId, String tagName)
    {
        return doForRepository(() -> m_userTagRepository.findAllByUserIdAndTagName(userId, tagName), "ProjectTagServiceHelper::getAllUserTagByUserIdAndTagName");
    }
}
