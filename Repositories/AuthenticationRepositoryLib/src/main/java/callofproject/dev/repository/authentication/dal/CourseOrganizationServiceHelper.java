package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.CourseOrganization;
import callofproject.dev.repository.authentication.repository.rdbms.ICourseOrganizationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.COURSE_ORGANIZATION_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.COURSE_ORGANIZATION_REPOSITORY_BEAN;

@Component(COURSE_ORGANIZATION_DAL_BEAN)
@Lazy
public class CourseOrganizationServiceHelper
{
    private final ICourseOrganizationRepository m_courseOrganizationRepository;

    public CourseOrganizationServiceHelper(@Qualifier(COURSE_ORGANIZATION_REPOSITORY_BEAN) ICourseOrganizationRepository courseOrganizationRepository)
    {
        m_courseOrganizationRepository = courseOrganizationRepository;
    }

    public Iterable<CourseOrganization> findAllByIds(Iterable<UUID> ids)
    {
        return m_courseOrganizationRepository.findAllById(ids);
    }

    public void removeCourseOrganizations(Iterable<CourseOrganization> courseOrganizations)
    {
        m_courseOrganizationRepository.deleteAll(courseOrganizations);
    }

    public CourseOrganization saveCourseOrganization(CourseOrganization courseOrganization)
    {
        return m_courseOrganizationRepository.save(courseOrganization);
    }

    public Optional<CourseOrganization> findByCourseOrganizationNameContainsIgnoreCase(String courseOrganizationName)
    {
        return m_courseOrganizationRepository.findByCourseOrganizationNameContainsIgnoreCase(courseOrganizationName);
    }

    public boolean existsByCourseOrganizationNameContainsIgnoreCase(String courseOrganizationName)
    {
        return m_courseOrganizationRepository.existsByCourseOrganizationNameContainsIgnoreCase(courseOrganizationName);
    }

    public void removeCourseOrganization(CourseOrganization courseOrganization)
    {
        m_courseOrganizationRepository.delete(courseOrganization);
    }

    public void removeCourseOrganizationById(UUID uuid)
    {
        m_courseOrganizationRepository.deleteById(uuid);
    }

    public Optional<CourseOrganization> findByIdOrganization(UUID id)
    {
        return m_courseOrganizationRepository.findById(id);
    }

    public Iterable<CourseOrganization> findAllCourseOrganizations()
    {
        return m_courseOrganizationRepository.findAll();
    }
}
