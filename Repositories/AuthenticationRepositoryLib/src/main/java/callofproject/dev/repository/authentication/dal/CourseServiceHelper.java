package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Course;
import callofproject.dev.repository.authentication.repository.rdbms.ICourseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.COURSE_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.COURSE_REPOSITORY_BEAN;

@Component(COURSE_DAL_BEAN)
@Lazy
public class CourseServiceHelper
{
    private final ICourseRepository m_courseRepository;

    public CourseServiceHelper(@Qualifier(COURSE_REPOSITORY_BEAN) ICourseRepository courseRepository)
    {
        m_courseRepository = courseRepository;
    }

    public Iterable<Course> findAllByIds(Iterable<UUID> ids)
    {
        return m_courseRepository.findAllById(ids);
    }
    public boolean existsByCourseNameContainsIgnoreCase(String courseName)
    {
        return m_courseRepository.existsByCourseNameContainsIgnoreCase(courseName);
    }

    public Optional<Course> findByCourseNameContainsIgnoreCase(String courseName)
    {
        return m_courseRepository.findByCourseNameContainsIgnoreCase(courseName);
    }

    public void removeCourses(Iterable<Course> courses)
    {
        m_courseRepository.deleteAll(courses);
    }
    public Course saveCourse(Course course)
    {
        return m_courseRepository.save(course);
    }

    public void removeCourse(Course course)
    {
        m_courseRepository.delete(course);
    }

    public void removeCourseById(UUID id)
    {
        m_courseRepository.deleteById(id);
    }

    public Optional<Course> findById(UUID id)
    {
        return m_courseRepository.findById(id);
    }

    public Iterable<Course> findAll()
    {
        return m_courseRepository.findAll();
    }
}
