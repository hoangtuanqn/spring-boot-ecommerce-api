package mst.local.mstsoftware.modules.user.repositories;

import mst.local.mstsoftware.modules.user.entities.Role;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType role);

    @Query("""
                SELECT r FROM Role r
                LEFT JOIN FETCH r.permissions
                WHERE r.name = :name
            """)
    Optional<Role> findByNameWithPermissions(@Param("name") RoleType name);

    boolean existsByName(String name);
}
