package callofproject.dev.authentication;

import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.dto.user_profile.*;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Util
{
    private Util()
    {
    }

    public static UserSaveDTO createUserSaveDTO()
    {
        return new UserSaveDTO(
                "your_access_token_here",
                "your_refresh_token_here",
                true,
                UUID.randomUUID()
        );
    }

    public static UserSignUpRequestDTO createUserSignUpRequestDTO()
    {
        return new UserSignUpRequestDTO(
                "example@email.com",
                "John",
                "Doe",
                "Smith",
                "johndoe",
                "password123",
                LocalDate.of(1990, 5, 15),
                RoleEnum.ROLE_USER
        );
    }

    public static UserProfileUpdateDTO createUserProfileUpdateDTO()
    {
        return new UserProfileUpdateDTO(
                10,
                10,
                UUID.randomUUID().toString(),
                "About me text"
        );
    }

    public static UserProfile createUserProfile()
    {
        return new UserProfile(
                "Sample CV Text",
                "https://example.com/profile.jpg",
                "About me information"
        );
    }

    public static UserDTO createUserDTO()
    {
        return new UserDTO(
                "ahmetkoc",
                "johndoe@example.com",
                "John",
                "M",
                "Doe"
        );
    }

    public static UsersDTO createUsersDTO()
    {
        return new UsersDTO(List.of(createUserDTO()));
    }

    public static UserProfileDTO createUserProfileDTO()
    {
        var userRateDTO = new UserRateDTO(4.5, 4.2);

        var course1 = new CourseDTO(
                UUID.randomUUID(),
                "Example University",
                "Computer Science 101",
                LocalDate.of(2022, 9, 1),
                LocalDate.of(2023, 5, 30),
                false,
                "This is a computer science course."
        );

        var course2 = new CourseDTO(
                UUID.randomUUID(),
                "Another College",
                "Mathematics Fundamentals",
                LocalDate.of(2022, 10, 15),
                LocalDate.of(2023, 6, 10),
                false,
                "Fundamental math concepts."
        );

        var education1 = new EducationDTO(
                UUID.randomUUID(),
                "University of Example",
                "Computer Science",
                "Bachelor's Degree",
                LocalDate.of(2018, 9, 1),
                LocalDate.of(2022, 6, 30),
                false,
                3.8
        );

        var education2 = new EducationDTO(
                UUID.randomUUID(),
                "College of Another",
                "Mathematics",
                "Associate's Degree",
                LocalDate.of(2016, 9, 1),
                LocalDate.of(2018, 6, 30),
                false,
                3.5
        );

        var experience1 = new ExperienceDTO(
                UUID.randomUUID(),
                "Tech Company XYZ",
                "Worked on software development projects.",
                "https://www.example.com/xyz",
                LocalDate.of(2020, 5, 1),
                LocalDate.of(2022, 7, 15),
                false,
                "Software Engineer"
        );

        var experience2 = new ExperienceDTO(
                UUID.randomUUID(),
                "Startup ABC",
                "Led a team of developers.",
                "https://www.example.com/abc",
                LocalDate.of(2018, 3, 15),
                LocalDate.of(2020, 4, 30),
                false,
                "Team Lead"
        );

        var link1 = new LinkDTO(1, "Portfolio Website", "https://www.example.com/portfolio");
        var link2 = new LinkDTO(2, "LinkedIn Profile", "https://www.linkedin.com/in/johndoe");

        List<CourseDTO> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        List<EducationDTO> educations = new ArrayList<>();
        educations.add(education1);
        educations.add(education2);


        List<ExperienceDTO> experiences = new ArrayList<>();
        experiences.add(experience1);
        experiences.add(experience2);

        List<LinkDTO> links = new ArrayList<>();
        links.add(link1);
        links.add(link2);

        var userProfileDTO = new UserProfileDTO();
        userProfileDTO.cv = "Sample CV Text";
        userProfileDTO.profilePhoto = "https://example.com/profile.jpg";
        userProfileDTO.aboutMe = "About me information";
        userProfileDTO.courses = courses;
        userProfileDTO.educations = educations;
        userProfileDTO.experiences = experiences;
        userProfileDTO.links = links;

        return userProfileDTO;
    }
}
