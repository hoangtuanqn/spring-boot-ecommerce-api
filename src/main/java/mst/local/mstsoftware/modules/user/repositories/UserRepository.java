package mst.local.mstsoftware.modules.user.repositories;

import mst.local.mstsoftware.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
                SELECT DISTINCT u FROM User u
                LEFT JOIN FETCH u.userRoles ur
                LEFT JOIN FETCH ur.role r
                LEFT JOIN FETCH r.permissions
                WHERE u.email = :email
            """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
