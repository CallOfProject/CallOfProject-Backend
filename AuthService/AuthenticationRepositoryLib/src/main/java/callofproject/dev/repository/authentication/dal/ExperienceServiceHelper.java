package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Experience;
import callofproject.dev.repository.authentication.repository.rdbms.IExperienceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.EXPERIENCE_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.EXPERIENCE_REPOSITORY_BEAN;

@Component(EXPERIENCE_DAL_BEAN)
@Lazy
public class ExperienceServiceHelper
{
    private final IExperienceRepository m_experienceRepository;

    ExperienceServiceHelper(@Qualifier(EXPERIENCE_REPOSITORY_BEAN) IExperienceRepository experienceRepository)
    {
        m_experienceRepository = experienceRepository;
    }

    public Experience saveExperience(Experience experience)
    {
        return m_experienceRepository.save(experience);
    }

    public void removeExperience(Experience experience)
    {
        m_experienceRepository.delete(experience);
    }

    public void removeExperienceById(UUID id)
    {
        m_experienceRepository.deleteById(id);
    }

    public Optional<Experience> findById(UUID id)
    {
        return m_experienceRepository.findById(id);
    }

    public Iterable<Experience> findAll()
    {
        return m_experienceRepository.findAll();
    }
}
