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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;

@Component
@Lazy
@SuppressWarnings("all")
public class MatchServiceHelper
{
    private final IMatchDbRepository m_matchDbRepository;

    public MatchServiceHelper(IMatchDbRepository matchDbRepository)
    {
        m_matchDbRepository = matchDbRepository;
    }

    public UserMatch saveUserMatch(UserMatch userMatch)
    {
        return doForRepository(() -> m_matchDbRepository.save(userMatch), "MatchServiceHelper::saveUserMatch");
    }

    public void removeUserMatch(UserMatch userMatch)
    {
        doForRepository(() -> m_matchDbRepository.delete(userMatch), "MatchServiceHelper::removeUserMatch");
    }

    public void removeUserMatchById(UUID id)
    {
        doForRepository(() -> m_matchDbRepository.deleteById(id), "MatchServiceHelper::removeUserMatchById");
    }

    public long count()
    {
        return doForRepository(() -> m_matchDbRepository.count(), "MatchServiceHelper::count");
    }

    public Iterable<UserMatch> saveAll(Iterable<UserMatch> userMatches)
    {
        return doForRepository(() -> m_matchDbRepository.saveAll(userMatches), "MatchServiceHelper::saveAll");
    }

    public Optional<UserMatch> getUserMatchById(UUID id)
    {
        return doForRepository(() -> m_matchDbRepository.findById(id), "MatchServiceHelper::getUserMatchById");
    }

    public Iterable<UserMatch> getAllUserMatch()
    {
        return doForRepository(() -> m_matchDbRepository.findAll(), "MatchServiceHelper::getAllUserMatch");
    }

    public void removeAllUserMatch()
    {
        doForRepository(() -> m_matchDbRepository.deleteAll(), "MatchServiceHelper::removeAllUserMatch");
    }

    public void removeAllUserMatch(Iterable<UserMatch> userMatches)
    {
        doForRepository(() -> m_matchDbRepository.deleteAll(userMatches), "MatchServiceHelper::removeAllUserMatch");
    }

    public void removeUserMatchByUserId(UUID userId)
    {
        doForRepository(() -> m_matchDbRepository.deleteByUserID(userId), "MatchServiceHelper::removeUserMatchByUserId");
    }

    public Iterable<UserMatch> getUserMatchByUserId(UUID userId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByUserID(userId), "MatchServiceHelper::getUserMatchByUserId");
    }

    public Iterable<UserMatch> getUserMatchBySchoolId(long schoolId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolId(schoolId), "MatchServiceHelper::getUserMatchBySchoolId");
    }

    public Iterable<UserMatch> getUserMatchByCourseId(UUID courseId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByCourseId(courseId), "MatchServiceHelper::getUserMatchByCourseId");
    }

    public Iterable<UserMatch> getUserMatchByExperienceId(UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByExperienceId(experienceId), "MatchServiceHelper::getUserMatchByExperienceId");
    }

    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseId(long schoolId, UUID courseId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolIdAndCourseId(schoolId, courseId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseId");
    }

    public Iterable<UserMatch> getUserMatchBySchoolIdAndExperienceId(long schoolId, UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolIdAndExperienceId(schoolId, experienceId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndExperienceId");
    }

    public Iterable<UserMatch> getUserMatchByCourseIdAndExperienceId(UUID courseId, UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllByCourseIdAndExperienceId(courseId, experienceId),
                "MatchServiceHelper::getUserMatchByCourseIdAndExperienceId");
    }

    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceId(long schoolId, UUID courseId, UUID experienceId)
    {
        return doForRepository(() -> m_matchDbRepository.findAllBySchoolIdAndCourseIdAndExperienceId(schoolId, courseId, experienceId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseIdAndExperienceId");
    }

    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserId(long schoolId, UUID courseId, UUID experienceId, UUID userId)
    {
        return doForRepository(() -> m_matchDbRepository.getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserID(schoolId, courseId, experienceId, userId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserId");
    }

    public Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIdAndMatchId(long schoolId, UUID courseId, UUID experienceId, UUID userId, UUID matchId)
    {
        return doForRepository(() -> m_matchDbRepository.getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIDAndMatchId(schoolId, courseId, experienceId, userId, matchId),
                "MatchServiceHelper::getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIdAndMatchId");
    }
}
