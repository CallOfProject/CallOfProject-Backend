package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Link;
import callofproject.dev.repository.authentication.repository.rdbms.ILinkRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static callofproject.dev.repository.authentication.BeanName.LINK_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.LINK_REPOSITORY_BEAN;

@Component(LINK_DAL_BEAN)
@Lazy
public class LinkServiceHelper
{
    private final ILinkRepository m_linkRepository;

    public LinkServiceHelper(@Qualifier(LINK_REPOSITORY_BEAN) ILinkRepository linkRepository)
    {
        m_linkRepository = linkRepository;
    }

    public ILinkRepository getLinkRepository()
    {
        return m_linkRepository;
    }

    public Link saveLink(Link link)
    {
        return m_linkRepository.save(link);
    }

    public void removeLink(Link link)
    {
        m_linkRepository.delete(link);
    }

    public void removeLinkById(long id)
    {
        m_linkRepository.deleteById(id);
    }

    public Optional<Link> findById(long id)
    {
        return m_linkRepository.findById(id);
    }

    public Iterable<Link> findAll()
    {
        return m_linkRepository.findAll();
    }
}
