/*----------------------------------------------------------------
	FILE		: IMatchDbRepository.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	IMatchDbRepository interface represent the repository layer of the UserMatch entity.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.repository;


import callofproject.dev.nosql.entity.UserMatch;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.nosql.NoSqlBeanName.MATCH_REPOSITORY_BEAN_NAME;

/**
 * IMatchDbRepository interface represent the repository layer of the UserMatch entity.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Repository(MATCH_REPOSITORY_BEAN_NAME)
@Lazy
public interface IMatchDbRepository extends MongoRepository<UserMatch, UUID>
{
    /**
     * Delete user match by match id
     *
     * @param userID (user id is the id of the user who is matched with the user)
     */
    void deleteByUserID(UUID userID);

    /**
     * Get user match by user id
     *
     * @param userID (user id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllByUserID(UUID userID);

    /**
     * Get user match by user id
     *
     * @param schoolId (school id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */

    Iterable<UserMatch> findAllBySchoolId(long schoolId);

    /**
     * Get user match by user id and course id
     *
     * @param courseId (course id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllByCourseId(UUID courseId);

    /**
     * Get user match by experience id
     *
     * @param experienceId (experience id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllByExperienceId(UUID experienceId);

    /**
     * Get user match by school id and user id
     *
     * @param schoolId (school id is the id of the user who is matched with the user)
     * @param courseId (course id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllBySchoolIdAndCourseId(long schoolId, UUID courseId);

    /**
     * Get user match by school id and experience id
     *
     * @param schoolId     (school id is the id of the user who is matched with the user)
     * @param experienceId (experience id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllBySchoolIdAndExperienceId(long schoolId, UUID experienceId);

    /**
     * Get user match by school id, course id and user id
     *
     * @param experienceId (experience id is the id of the user who is matched with the user)
     * @param courseId     (course id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllByCourseIdAndExperienceId(UUID courseId, UUID experienceId);

    /**
     * Get user match by school id, course id and experience id
     *
     * @param schoolId     (school id is the id of the user who is matched with the user)
     * @param courseId     (course id is the id of the user who is matched with the user)
     * @param experienceId (experience id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> findAllBySchoolIdAndCourseIdAndExperienceId(long schoolId, UUID courseId, UUID experienceId);

    /**
     * Get user match by school id, course id, experience id and user id
     *
     * @param schoolId     (school id is the id of the user who is matched with the user)
     * @param courseId     (course id is the id of the user who is matched with the user)
     * @param experienceId (experience id is the id of the user who is matched with the user)
     * @param userId       (user id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserID(long schoolId, UUID courseId, UUID experienceId, UUID userId);

    /**
     * Get user match by school id, course id, experience id, user id and match id
     *
     * @param schoolId     (school id is the id of the user who is matched with the user)
     * @param courseId     (course id is the id of the user who is matched with the user)
     * @param experienceId (experience id is the id of the user who is matched with the user)
     * @param userId       (user id is the id of the user who is matched with the user)
     * @param matchId      (match id is the id of the user who is matched with the user)
     * @return UserMatch iterable.
     */
    //Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIDAndMatchId(long schoolId, UUID courseId, UUID experienceId, UUID userId, UUID matchId);
}
