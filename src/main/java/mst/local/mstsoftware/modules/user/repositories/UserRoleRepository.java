package mst.local.mstsoftware.modules.user.repositories;

import mst.local.mstsoftware.modules.user.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
}
