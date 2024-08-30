package be.com.bemedicare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {


    @Bean
    JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
        mailSender.setHost("smtp.nate.com");
        mailSender.setPort(587);
        mailSender.setUsername("yoohwanjoo@nate.com");
        mailSender.setPassword("aa6842053");
//        mailSender.setUsername("wellnesspring77@gmail.com");
//        mailSender.setPassword("hgmgnkjgytipghww");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}