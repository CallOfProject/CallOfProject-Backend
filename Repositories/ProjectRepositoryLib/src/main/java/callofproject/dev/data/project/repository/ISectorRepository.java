package callofproject.dev.data.project.repository;

import callofproject.dev.data.project.entity.Sector;
import callofproject.dev.data.project.entity.enums.ESector;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.SECTOR_REPOSITORY;

@Repository(SECTOR_REPOSITORY)
@Lazy
public interface ISectorRepository extends CrudRepository<Sector, Long>
{
    @Query("SELECT s FROM Sector s WHERE s.m_sector = :sector")
    Optional<Sector> findSectorBySector(ESector sector);
}
