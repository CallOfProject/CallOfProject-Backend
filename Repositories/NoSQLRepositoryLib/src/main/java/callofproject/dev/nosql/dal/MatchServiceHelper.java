/*----------------------------------------------------------------
	FILE		: MatchServiceHelper.java
	AUTHOR		: Nuri Can OZTURK
	LAST UPDATE	: 22.11.2023
	MatchServiceHelper class represent the helper class of the MatchService.
	Copyleft (c) NoSQLRepository.
	All Rights Free
----------------------------------------------------------------*/
package callofproject.dev.nosql.dal;

import callofproject.dev.nosql.entity.UserMatch;
import callofproject.dev.nosql.repository.IMatchDbRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.nosql.NoSqlBeanName.MATCH_REPOSITORY_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.MATCH_SERVICE_HELPER_BEAN_NAME;

@Component(MATCH_SERVICE_HELPER_BEAN_NAME)
@Lazy
@SuppressWarnings("all")
public class MatchServiceHelper
{
    private final IMatchDbRepository m_matchDbRepository;


    public MatchServiceHelper(@Qualifier(MATCH_REPOSITORY_BEAN_NAME) IMatchDbRepository matchDbRepository)
    {
        m_matchDbRepository = matchDbRepository;
    }

    /**
     * Save the user match to the database.
     *
     * @param userMatch The user match to be saved.
     * @return The saved user match.
     */
    public UserMatch saveUserMatch(UserMatch userMatch)
    {
        return doForRepository(() -> m_matchDbRepository.save(userMatch), "MatchServiceHelper::saveUserMatch");
    }

    /**
     * Remove the user match from the database.
     *
     * @param userMatch The user match to be removed.
     */
    public void removeUserMatch(UserMatch userMatch)
    {
        doForRepository(() -> m_matchDbRepository.delete(userMatch), "MatchServiceHelper::removeUserMatch");
    }

    /**
     * Remove the user match from the database by id.
     *
     * @param id The id of the user match to be removed.
     */
    public void removeUserMatchById(UUID id)
    {
        doForRepository(() -> m_matchDbRepository.deleteById(id), "MatchServiceHelper::removeUserMatchById");
    }

    /**
     * Count the number of user match in the database.
     *
     * @return The number of user match in the database.
     */
    public long count()
    {
        return doForRepository(() -> m_matchDbRepository.count(), "MatchServiceHelper::count");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> saveAll(Iterable<UserMatch> userMatches)
    {
        return doForRepository(() -> m_matchDbRepository.saveAll(userMatches), "MatchServiceHelper::saveAll");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Optional<UserMatch> getUserMatchById(UUID id)
    {
        return doForRepository(() -> m_matchDbRepository.findById(id), "MatchServiceHelper::getUserMatchById");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getAllUserMatch()
    {
        return doForRepository(() -> m_matchDbRepository.findAll(), "MatchServiceHelper::getAllUserMatch");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public void removeAllUserMatch()
    {
        doForRepository(() -> m_matchDbRepository.deleteAll(), "MatchServiceHelper::removeAllUserMatch");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public void removeAllUserMatch(Iterable<UserMatch> userMatches)
    {
        doForRepository(() -> m_matchDbRepository.deleteAll(userMatches), "MatchServiceHelper::removeAllUserMatch");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public void removeUserMatchByUserId(UUID userId)
    {
        doForRepository(() -> m_matchDbRepository.deleteByUserID(userId), "MatchServiceHelper::removeUserMatchByUserId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchByUserId(UUID userId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByUserID(userId), "MatchServiceHelper::getUserMatchByUserId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchBySchoolId(long schoolId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolId(schoolId), "MatchServiceHelper::getUserMatchBySchoolId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchByCourseId(UUID courseId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByCourseId(courseId), "MatchServiceHelper::getUserMatchByCourseId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param id The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchByExperienceId(UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByExperienceId(experienceId), "MatchServiceHelper::getUserMatchByExperienceId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param schoolId The id of the user match to be checked.
     * @param courseId The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseId(long schoolId, UUID courseId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolIdAndCourseId(schoolId, courseId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param schoolId     The id of the user match to be checked.
     * @param experienceId The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchBySchoolIdAndExperienceId(long schoolId, UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolIdAndExperienceId(schoolId, experienceId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndExperienceId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param courseId     The id of the user match to be checked.
     * @param experienceId The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchByCourseIdAndExperienceId(UUID courseId, UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByCourseIdAndExperienceId(courseId, experienceId),
                "MatchServiceHelper::getUserMatchByCourseIdAndExperienceId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param schoolId     The id of the user match to be checked.
     * @param courseId     The id of the user match to be checked.
     * @param experienceId The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceId(long schoolId, UUID courseId, UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolIdAndCourseIdAndExperienceId(schoolId, courseId, experienceId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseIdAndExperienceId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param schoolId     The id of the user match to be checked.
     * @param courseId     The id of the user match to be checked.
     * @param experienceId The id of the user match to be checked.
     * @param userId       The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserId(long schoolId, UUID courseId, UUID experienceId, UUID userId)
    {
        return doForRepository(() -> m_matchDbRepository.getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserID(schoolId, courseId, experienceId, userId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserId");
    }

    /**
     * Check if the user match exists in the database by id.
     *
     * @param schoolId     The id of the user match to be checked.
     * @param courseId     The id of the user match to be checked.
     * @param experienceId The id of the user match to be checked.
     * @param userId       The id of the user match to be checked.
     * @param matchId      The id of the user match to be checked.
     * @return True if the user match exists in the database, false otherwise.
     */
    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIdAndMatchId(long schoolId, UUID courseId, UUID experienceId, UUID userId, UUID matchId)
    {
        return doForRepository(() -> m_matchDbRepository.getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIDAndMatchId(schoolId, courseId, experienceId, userId, matchId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIdAndMatchId");
    }
}
