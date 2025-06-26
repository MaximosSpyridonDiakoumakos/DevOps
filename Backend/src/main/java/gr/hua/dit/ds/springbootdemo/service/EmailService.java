package gr.hua.dit.ds.springbootdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendProjectUpdateEmail(String to, String projectTitle, String updateContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Update for Project: " + projectTitle);
        message.setText("There's a new update for the project you're supporting:\n\n" +
                      "Project: " + projectTitle + "\n\n" +
                      "Update: " + updateContent);
        
        emailSender.send(message);
    }
} 