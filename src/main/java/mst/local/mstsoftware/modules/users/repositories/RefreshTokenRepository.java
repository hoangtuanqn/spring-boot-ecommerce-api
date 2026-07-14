package mst.local.mstsoftware.modules.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import mst.local.mstsoftware.modules.users.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
