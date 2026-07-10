package mst.local.mstsoftware.modules.users.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mst.local.mstsoftware.modules.users.entities.BlacklistedToken;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByTokenHash(String token);

    void deleteByExpiryDateBefore(LocalDateTime date);

    List<BlacklistedToken> findByUserId(Long userId);

}
