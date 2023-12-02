package callofproject.dev.nosql.dal;

import callofproject.dev.nosql.entity.Tag;
import callofproject.dev.nosql.repository.ITagRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.nosql.NoSqlBeanName.TAG_SERVICE_HELPER_BEAN_NAME;

/**
 * TagServiceHelper class represent the helper class of the TagService.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Component(TAG_SERVICE_HELPER_BEAN_NAME)
@Lazy
public class TagServiceHelper
{
    private final ITagRepository m_tagRepository;

    /**
     * Constructor
     *
     * @param tagRepository tag repository
     */
    public TagServiceHelper(ITagRepository tagRepository)
    {
        m_tagRepository = tagRepository;
    }

    /**
     * Exists by tag name contains ignore case
     *
     * @param tagName tag name
     * @return boolean value
     */
    public boolean existsByTagNameContainsIgnoreCase(String tagName)
    {
        return doForRepository(() -> m_tagRepository.existsByTagNameContainsIgnoreCase(tagName),
                "TagServiceHelper::existsByTagNameContainsIgnoreCase");
    }

    /**
     * Find by tag name contains ignore case
     *
     * @param tagName tag name
     * @return Tag object
     */
    public Optional<Tag> findByTagNameContainsIgnoreCase(String tagName)
    {
        return doForRepository(() -> m_tagRepository.findByTagNameContainsIgnoreCase(tagName),
                "TagServiceHelper::findByTagNameContainsIgnoreCase");
    }

    /**
     * Save tag if not exists
     *
     * @param tagName tag name
     * @return Tag object
     */
    public Tag saveTagIfNotExists(String tagName)
    {
        return doForRepository(() -> m_tagRepository.saveTagIfNotExists(tagName),
                "TagServiceHelper::saveTagIfNotExists");
    }

    /**
     * Save tag
     *
     * @param tag tag
     * @return Tag object
     */
    public Tag saveTag(Tag tag)
    {
        return doForRepository(() -> m_tagRepository.save(tag), "TagServiceHelper::saveTag");
    }
}
