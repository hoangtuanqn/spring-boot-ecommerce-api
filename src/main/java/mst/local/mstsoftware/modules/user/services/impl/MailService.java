package mst.local.mstsoftware.modules.user.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMailWelcome(String toEmail, String fullName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject("Chào mừng bạn đến với MST Software!");
        helper.setText(buildWelcomeContent(fullName), true); // true = nội dung là HTML
        helper.setFrom("no-reply@mstsoftware.vn");

        mailSender.send(message);
    }

    private String buildWelcomeContent(String fullName) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2>Xin chào %s,</h2>
                    <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>MST Software</strong>.</p>
                    <p>Chúc bạn có trải nghiệm tốt cùng chúng tôi.</p>
                </body>
                </html>
                """.formatted(fullName);
    }
}
