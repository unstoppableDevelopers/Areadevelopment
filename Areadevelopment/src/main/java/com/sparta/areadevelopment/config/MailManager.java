package com.sparta.areadevelopment.config;


import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailManager {
    @Value("${MAIL_USERNAME}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String text) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setFrom(sender);

        mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
        mimeMessage.setSubject(subject);
        mimeMessage.setText(text);
        mailSender.send(mimeMessage);
    }
}
