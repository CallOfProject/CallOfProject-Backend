package callofproject.dev.repository.usermanagement.dal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static callofproject.dev.repository.usermanagement.BeanName.*;

@Component(USER_MANAGEMENT_DAL_BEAN)
@Lazy
public class UserManagementServiceHelper
{
    private final UserServiceHelper m_userServiceHelper;
    private final UserProfileServiceHelper m_userProfileServiceHelper;
    private final EducationServiceHelper m_educationServiceHelper;
    private final CourseServiceHelper m_courseServiceHelper;
    private final LinkServiceHelper m_linkServiceHelper;
    private final ExperienceServiceHelper m_experienceServiceHelper;

    public UserManagementServiceHelper(@Qualifier(USER_DAL_BEAN) UserServiceHelper userServiceHelper,
                                       @Qualifier(USER_PROFILE_DAL_BEAN) UserProfileServiceHelper userProfileServiceHelper,
                                       @Qualifier(EDUCATION_DAL_BEAN) EducationServiceHelper educationServiceHelper,
                                       @Qualifier(COURSE_DAL_BEAN) CourseServiceHelper courseServiceHelper,
                                       @Qualifier(LINK_DAL_BEAN) LinkServiceHelper linkServiceHelper,
                                       @Qualifier(EXPERIENCE_DAL_BEAN) ExperienceServiceHelper experienceServiceHelper)
    {
        m_userServiceHelper = userServiceHelper;
        m_userProfileServiceHelper = userProfileServiceHelper;
        m_educationServiceHelper = educationServiceHelper;
        m_courseServiceHelper = courseServiceHelper;
        m_linkServiceHelper = linkServiceHelper;
        m_experienceServiceHelper = experienceServiceHelper;
    }

    public UserServiceHelper getUserServiceHelper()
    {
        return m_userServiceHelper;
    }

    public UserProfileServiceHelper getUserProfileServiceHelper()
    {
        return m_userProfileServiceHelper;
    }

    public EducationServiceHelper getEducationServiceHelper()
    {
        return m_educationServiceHelper;
    }

    public CourseServiceHelper getCourseServiceHelper()
    {
        return m_courseServiceHelper;
    }

    public LinkServiceHelper getLinkServiceHelper()
    {
        return m_linkServiceHelper;
    }

    public ExperienceServiceHelper getExperienceServiceHelper()
    {
        return m_experienceServiceHelper;
    }
}
