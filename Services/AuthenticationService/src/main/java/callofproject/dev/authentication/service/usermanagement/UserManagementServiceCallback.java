package callofproject.dev.authentication.service.usermanagement;

import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.dto.user_profile.UserProfileDTO;
import callofproject.dev.authentication.dto.user_profile.UserWithProfileDTO;
import callofproject.dev.authentication.mapper.MapperConfiguration;
import callofproject.dev.authentication.service.ImageService;
import callofproject.dev.authentication.service.S3Service;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.common.util.UtilityMethod;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.entity.UserTag;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.data.common.util.UtilityMethod.convert;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toListConcurrent;
import static java.lang.String.format;

@Service
@Lazy
public class UserManagementServiceCallback
{
    private final KafkaProducer m_userProducer;
    private final UserManagementServiceHelper m_serviceHelper;
    private final MapperConfiguration m_mapperConfig;
    private final S3Service m_storageService;
    private final ImageService m_imageService;

    @Value("${user.profile.default_pp}")
    private String defaultProfilePhoto;

    /**
     * Instantiates a new User management service callback.
     *
     * @param userProducer   the user producer
     * @param serviceHelper  the service helper
     * @param mapperConfig   the mapper config
     * @param storageService the storage service
     * @param imageService   the image service
     */
    public UserManagementServiceCallback(KafkaProducer userProducer, UserManagementServiceHelper serviceHelper, MapperConfiguration mapperConfig, S3Service storageService, ImageService imageService)
    {
        m_userProducer = userProducer;
        m_serviceHelper = serviceHelper;
        m_mapperConfig = mapperConfig;
        m_storageService = storageService;
        m_imageService = imageService;
    }

    /**
     * Save user with given dto class.
     *
     * @param userDTO represent the user dto class.
     * @return UserSaveDTO class.
     */
    public ResponseMessage<UserSaveDTO> saveUserCallback(UserSignUpRequestDTO userDTO)
    {
        var user = m_mapperConfig.userMapper.toUser(userDTO);
        user.setAccountBlocked(true);

        var userProfile = new UserProfile();
        userProfile.setProfilePhoto(defaultProfilePhoto);
        userProfile.setUser(user);
        user.setUserProfile(userProfile);

        var savedUser = m_serviceHelper.getUserServiceHelper().saveUser(user);

        if (savedUser == null)
            throw new DataServiceException("User cannot be saved!");

        var claims = new HashMap<String, Object>();
        var authorities = JwtUtil.populateAuthorities(user.getRoles());
        claims.put("authorities", authorities);

        var token = JwtUtil.generateToken(claims, user.getUsername());
        var refreshToken = JwtUtil.generateToken(claims, user.getUsername());

        return new ResponseMessage<>("User saved successfully!", 200, new UserSaveDTO(token, refreshToken, true, savedUser.getUserId()));
    }


    /**
     * Save user with given dto class.
     *
     * @param userDTO represent the user dto class.
     * @return UserSaveDTO class.
     */
    public ResponseMessage<UserSaveDTO> saveUserForMobileCallback(UserSignUpRequestDTO userDTO)
    {
        var user = m_mapperConfig.userMapper.toUser(userDTO);
        user.setAccountBlocked(false);

        var userProfile = new UserProfile();
        userProfile.setProfilePhoto(defaultProfilePhoto);
        userProfile.setUser(user);
        user.setUserProfile(userProfile);

        var savedUser = m_serviceHelper.getUserServiceHelper().saveUser(user);

        if (savedUser == null)
            throw new DataServiceException("User cannot be saved!");

        var claims = new HashMap<String, Object>();
        var authorities = JwtUtil.populateAuthorities(user.getRoles());
        claims.put("authorities", authorities);

        var token = JwtUtil.generateToken(claims, user.getUsername());
        var refreshToken = JwtUtil.generateToken(claims, user.getUsername());

        return new ResponseMessage<>("User saved successfully!", 200, new UserSaveDTO(token, refreshToken, true, savedUser.getUserId()));
    }

    /**
     * Find User with given username but returns the user entity.
     *
     * @param username represent the username.
     * @return User class.
     */
    public UserResponseDTO<User> findUserByUsernameForAuthenticationServiceCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists");

        return new UserResponseDTO<User>(true, user.get());
    }


    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    public ResponseMessage<UserDTO> findUserByUsernameCallback(String username)
    {
        var user = doForDataService(() -> m_serviceHelper.getUserServiceHelper().findByUsername(username),
                "User does not exists!");

        return user.map(value -> new ResponseMessage<>("User found!", 200, m_mapperConfig.userMapper.toUserDTO(value)))
                .orElseGet(() -> new ResponseMessage<>("User does not exists!", 400, null));

    }

    /**
     * Find all users with given word and page.
     *
     * @param page represent the page.
     * @param word represent the containing word.
     * @return UsersDTO class.
     */
    public MultipleResponseMessagePageable<Object> findAllUsersPageableByContainsWordCallback(int page, String word)
    {
        var userListPageable = m_serviceHelper.getUserServiceHelper().findUsersByUsernameContainsIgnoreCase(word, page);

        var dtoList = m_mapperConfig.userMapper.toUsersDTO(toListConcurrent(userListPageable.getContent(), m_mapperConfig.userMapper::toUserDTO));

        var msg = format("%d user found!", dtoList.users().size());

        return new MultipleResponseMessagePageable<>(userListPageable.getTotalPages(), page, dtoList.users().size(), msg, dtoList);
    }


    /**
     * Update user profile with given dto class.
     *
     * @param dto          represent the dto class
     * @param profilePhoto represent the profile photo
     * @return MessageResponseDTO.
     */
    public ResponseMessage<Object> upsertUserProfileCallback(UserProfileUpdateDTO dto, MultipartFile profilePhoto, MultipartFile cvFile)
    {
        var user = getUserIfExists(UUID.fromString(dto.userId()));

        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(user.getUserId());

        if (userProfile.isEmpty())
            throw new DataServiceException("User profile does not exists!");

        var compressedPhoto = profilePhoto != null && profilePhoto.getSize() > 0 ? m_imageService.compressImageToJPEG(profilePhoto) : null;
        var cvUrl = cvFile != null && cvFile.getSize() > 0 ? uploadCV(cvFile, userProfile.get(), user) : null;

        var profilePhotoUrl = compressedPhoto != null ? uploadProfilePhoto(compressedPhoto, userProfile.get(), user) : null;

        if (profilePhotoUrl != null)
            userProfile.get().setProfilePhoto(profilePhotoUrl);

        if (cvUrl != null)
            userProfile.get().setCv(cvUrl);

        userProfile.get().setAboutMe(dto.aboutMe());
        userProfile.get().setUserRate(dto.userRate());
        userProfile.get().setUserFeedbackRate(dto.userFeedbackRate());

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("User profile updated successfully!", 200, getUserProfile(user));
    }


    private String uploadProfilePhoto(byte[] profilePhoto, UserProfile userProfile, User user)
    {
        var fileName = "pp_" + user.getUserId() + "_" + userProfile.getUserProfileId() + "_" + System.currentTimeMillis() + ".jpg";
        return m_storageService.uploadToS3WithByteArray(profilePhoto, fileName, Optional.empty());
    }

    private String uploadCV(MultipartFile cv, UserProfile userProfile, User user)
    {
        var fileNameSplit = Objects.requireNonNull(cv.getOriginalFilename()).split("\\.");
        var extension = fileNameSplit[fileNameSplit.length - 1];
        var fileName = "cv_" + user.getUserId() + "_" + userProfile.getUserProfileId() + "_" + System.currentTimeMillis() + "." + extension;

        return m_storageService.uploadToS3WithMultiPartFileV2(cv, fileName, Optional.of("callofproject-cv"));
    }

    /**
     * Get user with given user id.
     *
     * @param userId represent the user id.
     * @return User class.
     */
    public User getUserIfExists(UUID userId)
    {
        var user = m_serviceHelper.getUserServiceHelper().findById(userId);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        return user.get();
    }


    /**
     * publish user to kafka
     *
     * @param uuid represent the uuid
     */
    public void PublishUser(UUID uuid)
    {
        var user = getUserIfExists(uuid);

        var kafkaMessage = new UserKafkaDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getFirstName(),
                user.getMiddleName(), user.getLastName(), EOperation.CREATE, user.getPassword(), user.getRoles(),
                user.getDeleteAt(), 0, 0, 0, user.getUserProfile().getProfilePhoto());

        m_userProducer.sendMessage(kafkaMessage);
    }


    /**
     * Find user profile with given user id.
     *
     * @param userId represent the user id.
     * @return UserProfileDTO class.
     */
    public ResponseMessage<Object> findUserProfileByUserIdCallback(UUID userId)
    {
        var profileMap = getUserProfile(getUserIfExists(userId));

        return new ResponseMessage<>("User profile found!", 200, profileMap);
    }

    /**
     * Find user profile with given username.
     *
     * @param username represent the username.
     * @return UserProfileDTO class.
     */
    public ResponseMessage<Object> findUserProfileByUserUsernameCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists!");

        var profileMap = getUserProfile(user.get());

        return new ResponseMessage<>("User profile found!", 200, profileMap);
    }


    /**
     * Find user and his/her profile with given id.
     *
     * @param uuid represent the user id.
     * @return UserWithProfileDTO class.
     */
    public ResponseMessage<Object> findUserWithProfileCallback(UUID uuid)
    {
        var user = getUserIfExists(uuid);
        var userProfile = getUserProfile(user);
        var userWithProfile = new UserWithProfileDTO(user.getUserId(), m_mapperConfig.userMapper.toUserDTO(user), userProfile);

        return new ResponseMessage<>("User with profile found!", 200, userWithProfile);
    }

    /**
     * Get user profile with given user.
     *
     * @param user represent the user.
     * @return UserProfileDTO class.
     */
    private UserProfileDTO getUserProfile(User user)
    {
        var userProfile = user.getUserProfile();

        var educations = m_mapperConfig.educationMapper.toEducationsDTO(toListConcurrent(userProfile.getEducationList(), m_mapperConfig.educationMapper::toEducationDTO));

        var experiences = m_mapperConfig.experienceMapper.toExperiencesDTO(toListConcurrent(userProfile.getExperienceList(), m_mapperConfig.experienceMapper::toExperienceDTO));

        var courses = m_mapperConfig.courseMapper.toCoursesDTO(toListConcurrent(userProfile.getCourseList(), m_mapperConfig.courseMapper::toCourseDTO));

        var links = m_mapperConfig.linkMapper.toLinksDTO(toListConcurrent(userProfile.getLinkList(), m_mapperConfig.linkMapper::toLinkDTO));

        var tags = m_mapperConfig.userTagMapper.toUserTagsDTO(toListConcurrent(userProfile.getTagList(), m_mapperConfig.userTagMapper::toUserTagDTO));

        return m_mapperConfig.userProfileMapper.toUserProfileDTO(userProfile, educations, experiences, courses, links, tags);
    }

    public ResponseMessage<Object> updateAboutMeCallback(UUID userId, String aboutMe)
    {
        var user = getUserIfExists(userId);

        var userProfile = m_serviceHelper.getUserProfileServiceHelper().findUserProfileByUserId(user.getUserId());

        if (userProfile.isEmpty())
            throw new DataServiceException("User profile does not exists!");

        userProfile.get().setAboutMe(aboutMe);

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(userProfile.get());

        return new ResponseMessage<>("About me updated successfully!", 200, getUserProfile(user));
    }

    public ResponseMessage<Object> uploadUserProfilePhotoCallback(UUID userId, MultipartFile file)
    {
        var user = getUserIfExists(userId);

        var compressedPhoto = file != null && file.getSize() > 0 ? m_imageService.compressImageToJPEG(file) : null;

        var profilePhotoUrl = compressedPhoto != null ? uploadProfilePhoto(compressedPhoto, user.getUserProfile(), user) : null;

        user.getUserProfile().setProfilePhoto(profilePhotoUrl);

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(user.getUserProfile());

        return new ResponseMessage<>("Profile photo uploaded successfully!", 200, profilePhotoUrl);
    }

    public ResponseMessage<Object> uploadUserCvCallback(UUID userId, MultipartFile cvFile)
    {
        var user = getUserIfExists(userId);

        var cvUrl = cvFile != null && cvFile.getSize() > 0 ? uploadCV(cvFile, user.getUserProfile(), user) : null;

        user.getUserProfile().setCv(cvUrl);

        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(user.getUserProfile());

        return new ResponseMessage<>("CV uploaded successfully!", 200, cvUrl);
    }


    public ResponseMessage<Object> createUserTagCallback(UserTagCreateDTO dto)
    {
        var user = getUserIfExists(dto.userId());

        // to english alphabet and create UserTag object
        var tagNameObjList = dto.tagNames().stream()
                .map(UtilityMethod::convert)
                .map(UserTag::new)
                .toList();

        var addingTags = tagNameObjList.stream()
                .filter(tag -> !user.getUserProfile().getTagList().stream().map(UserTag::getTagName).toList()
                        .contains(tag.getTagName()))
                .toList();

        if (addingTags.isEmpty())
            return new ResponseMessage<>("User tag already exists!", 400, user.getUserProfile().getTagList()
                    .stream().map(m_mapperConfig.userTagMapper::toUserTagDTO).toList());
        // save all tags
        var savedUserTag = m_serviceHelper.getUserTagServiceHelper().saveAll(addingTags);

        // add tags to user profile
        savedUserTag.forEach(user.getUserProfile()::addTag);

        // save user and user profile
        m_serviceHelper.getUserServiceHelper().saveUser(user);
        m_serviceHelper.getUserProfileServiceHelper().saveUserProfile(user.getUserProfile());

        var tagsDTO = m_mapperConfig.userTagMapper.toUserTagsDTO(toListConcurrent(savedUserTag, m_mapperConfig.userTagMapper::toUserTagDTO));
        return new ResponseMessage<>("User tag saved successfully!", 200, tagsDTO);
    }

    public ResponseMessage<Object> updateUserTagCallback(UserTagUpdateDTO dto)
    {
        var user = getUserIfExists(dto.userId());

        var tag = m_serviceHelper.getUserTagServiceHelper().findByTagId(dto.tagId());

        if (tag.isEmpty())
            return new ResponseMessage<>("User tag does not exists!", 400, null);

        tag.get().setTagName(convert(dto.tagName()));

        var updatedTag = m_serviceHelper.getUserTagServiceHelper().saveUserTag(tag.get());
        user.getUserProfile().getTagList().stream()
                .filter(t -> t.getTagId().equals(updatedTag.getTagId()))
                .findFirst()
                .ifPresent(t -> t.setTagName(updatedTag.getTagName()));

        m_serviceHelper.getUserServiceHelper().saveUser(user);
        return new ResponseMessage<>("User tag updated successfully!", 200, m_mapperConfig.userTagMapper.toUserTagDTO(tag.get()));
    }

    public ResponseMessage<Object> deleteUserTagCallback(UUID userId, UUID tagId)
    {
        var user = getUserIfExists(userId);

        var tag = m_serviceHelper.getUserTagServiceHelper().findByTagId(tagId);

        if (tag.isEmpty())
            return new ResponseMessage<>("User tag does not exists!", 400, null);

        user.getUserProfile().getTagList().removeIf(t -> t.getTagId().equals(tagId));

        m_serviceHelper.getUserServiceHelper().saveUser(user);

        return new ResponseMessage<>("User tag deleted successfully!", 200, m_mapperConfig.userTagMapper.toUserTagDTO(tag.get()));
    }
}
