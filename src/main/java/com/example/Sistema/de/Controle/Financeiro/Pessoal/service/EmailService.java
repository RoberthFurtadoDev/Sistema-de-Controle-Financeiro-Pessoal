// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/service/EmailService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("roberth.furtado@gmail.com"); // <--- DEVE SER EXATAMENTE IGUAL AO spring.mail.username
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("E-mail de redefinição enviado para: " + toEmail);
    }
}