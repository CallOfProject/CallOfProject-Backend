package callofproject.dev.data.community.repository;

import callofproject.dev.data.community.entity.Community;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Lazy
public interface ICommunityRepository extends CrudRepository<Community, UUID>
{
    Iterable<Community> findAllByCommunityNameContainsIgnoreCase(String communityName);

    @Query("from Community where communityName = :communityName and communityName = :communityType")
    Iterable<Community> findAllByExactCommunityName(String communityName, String communityType);

   /* @Query("from Community where project.m_projectId = :projectId")
    Optional<Community> findByProjectId(@Param("projectId") UUID projectId);

    @Query("from Community where project.m_projectOwner.m_userId = :ownerId")
    Optional<Community> findByProjectOwnerId(@Param("ownerId") UUID ownerId);*/
}
