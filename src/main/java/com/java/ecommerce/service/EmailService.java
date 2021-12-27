package com.java.ecommerce.service;

import com.java.ecommerce.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendEmail(String userMail, String subject, String message) {

        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(env.getProperty("spring.email.sender"));
        mailMessage.setTo(userMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);

    }
}
