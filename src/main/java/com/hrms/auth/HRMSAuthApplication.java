package com.hrms.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class HRMSAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(HRMSAuthApplication.class, args);
    }
    /* ─────────────────────────────────────
     *  MAIL STUB
     *  prints messages to the console until
     *  you configure real SMTP credentials
     * ───────────────────────────────────── 
    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl();   // console-only stub
    }*/


}
