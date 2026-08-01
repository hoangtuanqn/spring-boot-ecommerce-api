package mst.local.mstsoftware.cronjob;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.user.repositories.RefreshTokenRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@AllArgsConstructor
public class RefreshTokenCleanUpJob {
    private final RefreshTokenRepository repository;

    @Scheduled(cron = "${app.schedule.refresh-token-cleanup}")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("[TokenCleanup] Starting cleanup expired refresh tokens...");

        var countDeleted = repository.deleteByExpiryDateBefore(Instant.now());

        log.info("[TokenCleanup] Deleted {} expired tokens", countDeleted);
    }
}
