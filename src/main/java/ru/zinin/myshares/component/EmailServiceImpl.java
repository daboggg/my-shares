package ru.zinin.myshares.component;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {


    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alert@vzinin.ru");
        message.setTo("vzinin@list.ru");
        message.setSubject("TEST");
        message.setText("привет, это просто проверка, ура!!!");
        mailSender.send(message);
        System.out.println("DRDRDRDRDRDRDRDRDRRDRDRRDDR");
    }
}
