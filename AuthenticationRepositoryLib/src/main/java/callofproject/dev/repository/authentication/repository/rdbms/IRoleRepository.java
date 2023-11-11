package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.ROLE_REPOSITORY_BEAN;

@Repository(ROLE_REPOSITORY_BEAN)
@Lazy
public interface IRoleRepository extends CrudRepository<Role, Long>
{
    @Query("from Role as r where r.m_name = :name")
    List<Role> findRoleByName(@Param("name") String name);

    @Query(value = "insert user_roles (user_id, role_id) values (:userId, :roleId)", nativeQuery = true)
    void saveUserRole(@Param("userId") UUID userId, @Param("roleId") long roleId);
}
