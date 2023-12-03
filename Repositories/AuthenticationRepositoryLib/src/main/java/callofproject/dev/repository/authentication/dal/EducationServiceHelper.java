package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Education;
import callofproject.dev.repository.authentication.repository.rdbms.IEducationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.EDUCATION_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.EDUCATION_REPOSITORY_BEAN;

@Component(EDUCATION_DAL_BEAN)
@Lazy
public class EducationServiceHelper
{
    private final IEducationRepository m_educationRepository;

    public EducationServiceHelper(@Qualifier(EDUCATION_REPOSITORY_BEAN) IEducationRepository educationRepository)
    {
        m_educationRepository = educationRepository;
    }

    public Iterable<Education> findAllByIds(Iterable<UUID> ids)
    {
        return m_educationRepository.findAllById(ids);
    }

    public void removeEducations(Iterable<Education> educations)
    {
        m_educationRepository.deleteAll(educations);
    }
    public Education saveEducation(Education education)
    {
        return m_educationRepository.save(education);
    }

    public void removeEducation(Education education)
    {
        m_educationRepository.delete(education);
    }

    public void removeEducationById(UUID uuid)
    {
        m_educationRepository.deleteById(uuid);
    }

    public Optional<Education> findByIdEducation(UUID id)
    {
        return m_educationRepository.findById(id);
    }

    public Iterable<Education> findAllEducation()
    {
        return m_educationRepository.findAll();
    }
}
