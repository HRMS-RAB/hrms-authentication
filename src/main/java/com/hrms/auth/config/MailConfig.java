package com.hrms.auth.config;

import java.io.InputStream;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

@Configuration
public class MailConfig {

    /**
     * A dummy JavaMailSender that simply prints the SimpleMailMessage
     * to the console instead of actually connecting to an SMTP server.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {

            @Override
            public MimeMessage createMimeMessage() {
                // return an empty MimeMessage (no real session needed)
                return new MimeMessage((jakarta.mail.Session) null);
            }

            @Override
            public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
                try {
                    return new MimeMessage(null, contentStream);
                } catch (Exception e) {
                    throw new MailException("Failed to create mime message", e){};
                }
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                System.out.println("⮞ Sending email stub ⮜");
                System.out.println("To:     " + String.join(", ", simpleMessage.getTo()));
                System.out.println("Subject:" + simpleMessage.getSubject());
                System.out.println("Text:\n" + simpleMessage.getText());
                System.out.println("──────────────────────────────────");
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {
                for (var msg : simpleMessages) send(msg);
            }

            // noop for all the MimeMessage methods:
            @Override public void send(MimeMessage mimeMessage)              throws MailException {}
            @Override public void send(MimeMessage... mimeMessages)          throws MailException {}
            @Override public void send(MimeMessagePreparator mimePreparator) throws MailException {}
            @Override public void send(MimeMessagePreparator... mimePreparators) throws MailException {}
        };
    }
}
