package callofproject.dev.repository.usermanagement.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
public class UserProfile
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_profile_id")
    private UUID userProfileId;

    @Column(name = "cv")
    private String cv;
    @Column(name = "profile_photo")
    private String profilePhoto;
    @Column(name = "about_me", length = 500)
    private String aboutMe;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userProfile")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserRate userRate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_profiles_to_education",
            joinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "user_profile_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "education_id", referencedColumnName = "education_id", nullable = false))
    private Set<Education> educationList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_profiles_to_course", joinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "user_profile_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false))
    private Set<Course> courseList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_profiles_to_experience",
            joinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "user_profile_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "experience_id", referencedColumnName = "experience_id", nullable = false))
    private Set<Experience> experienceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_profiles_to_link",
            joinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "user_profile_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "link_id", nullable = false))
    private Set<Link> linkList;

    public UserProfile()
    {
    }

    public UserProfile(String cv, String profilePhoto, String aboutMe)
    {
        this.cv = cv;
        this.profilePhoto = profilePhoto;
        this.aboutMe = aboutMe;
    }

    public UUID getUserProfileId()
    {
        return userProfileId;
    }

    public void setUserProfileId(UUID userProfileId)
    {
        this.userProfileId = userProfileId;
    }

    public String getCv()
    {
        return cv;
    }

    public void setCv(String cv)
    {
        this.cv = cv;
    }

    public String getProfilePhoto()
    {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto)
    {
        this.profilePhoto = profilePhoto;
    }

    public String getAboutMe()
    {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe)
    {
        this.aboutMe = aboutMe;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public UserRate getUserRate()
    {
        return userRate;
    }

    public void setUserRate(UserRate userRate)
    {
        this.userRate = userRate;
    }

    public Set<Education> getEducationList()
    {
        return educationList;
    }

    public void setEducationList(Set<Education> educationList)
    {
        this.educationList = educationList;
    }

    public Set<Course> getCourseList()
    {
        return courseList;
    }

    public void setCourseList(Set<Course> courseList)
    {
        this.courseList = courseList;
    }

    public Set<Experience> getExperienceList()
    {
        return experienceList;
    }

    public void setExperienceList(Set<Experience> experienceList)
    {
        this.experienceList = experienceList;
    }

    public Set<Link> getLinkList()
    {
        return linkList;
    }

    public void setLinkList(Set<Link> linkList)
    {
        this.linkList = linkList;
    }

    public void addEducation(Education education)
    {
        if (educationList == null)
            educationList = new HashSet<>();

        educationList.add(education);
    }
}
