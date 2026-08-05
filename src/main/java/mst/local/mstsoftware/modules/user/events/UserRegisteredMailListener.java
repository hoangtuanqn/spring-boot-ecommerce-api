package mst.local.mstsoftware.modules.user.events;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.user.services.impl.MailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@AllArgsConstructor
@Component
public class UserRegisteredMailListener {
    private final MailService mailService;

    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event) {
        try {
            mailService.sendMailWelcome(event.getEmail(), event.getFullName());
        } catch (Exception ex) {
            // KHÔNG throw lại — lỗi gửi mail không được làm crash hệ thống
            log.error("Gửi mail chào mừng thất bại cho email={}", event.getEmail(), ex);
        }
    }
}
