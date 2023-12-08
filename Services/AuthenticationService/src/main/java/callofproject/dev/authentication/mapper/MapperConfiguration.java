package callofproject.dev.authentication.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static callofproject.dev.authentication.util.Util.MAPPER_CONFIG_BEAN;

@Component(MAPPER_CONFIG_BEAN)
@Lazy
public class MapperConfiguration
{
    public final ICourseMapper courseMapper;
    public final IEducationMapper educationMapper;
    public final IExperienceMapper experienceMapper;
    public final ILinkMapper linkMapper;
    public final IUserMapper userMapper;
    public final IUserProfileMapper userProfileMapper;
    public final IUserRateMapper userRateMapper;

    public MapperConfiguration(ICourseMapper courseMapper, IEducationMapper educationMapper, IExperienceMapper experienceMapper, ILinkMapper linkMapper, IUserMapper userMapper, IUserProfileMapper userProfileMapper, IUserRateMapper userRateMapper)
    {
        this.courseMapper = courseMapper;
        this.educationMapper = educationMapper;
        this.experienceMapper = experienceMapper;
        this.linkMapper = linkMapper;
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.userRateMapper = userRateMapper;
    }
}
