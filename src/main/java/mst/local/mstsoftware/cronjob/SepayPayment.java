package mst.local.mstsoftware.cronjob;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.helpers.Helpers;
import mst.local.mstsoftware.services.interfaces.SepayServiceInterface;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
@AllArgsConstructor
public class SepayPayment {
    private final SepayServiceInterface sepayService;
    private final ObjectMapper objectMapper;

    @Scheduled(cron = "${app.schedule.sepay-payment}")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("[TokenCleanup] Starting cleanup expired refresh tokens...");
        String fromDate = Helpers.formatDate(Instant.now().minus(300, ChronoUnit.DAYS));
        String toDate = Helpers.formatDate(Instant.now());

        var transactions = sepayService.getTransaction("259876543210", fromDate, toDate, 20);
        log.info("transaction" + objectMapper.writeValueAsString(transactions));
    }
}
