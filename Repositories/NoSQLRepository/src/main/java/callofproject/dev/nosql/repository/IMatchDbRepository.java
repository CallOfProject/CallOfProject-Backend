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

@Repository
@Lazy
public interface IMatchDbRepository extends MongoRepository<UserMatch, UUID>
{
    void deleteByUserID(UUID userID);

    Iterable<UserMatch> findAllByUserID(UUID userID);

    Iterable<UserMatch> findAllBySchoolId(long schoolId);

    Iterable<UserMatch> findAllByCourseId(UUID courseId);

    Iterable<UserMatch> findAllByExperienceId(UUID experienceId);

    Iterable<UserMatch> findAllBySchoolIdAndCourseId(long schoolId, UUID courseId);

    Iterable<UserMatch> findAllBySchoolIdAndExperienceId(long schoolId, UUID experienceId);

    Iterable<UserMatch> findAllByCourseIdAndExperienceId(UUID courseId, UUID experienceId);

    Iterable<UserMatch> findAllBySchoolIdAndCourseIdAndExperienceId(long schoolId, UUID courseId, UUID experienceId);

    Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserID(long schoolId, UUID courseId, UUID experienceId, UUID userId);

    Iterable<UserMatch> getUserMatchBySchoolIdAndCourseIdAndExperienceIdAndUserIDAndMatchId(long schoolId, UUID courseId, UUID experienceId, UUID userId, UUID matchId);
}
