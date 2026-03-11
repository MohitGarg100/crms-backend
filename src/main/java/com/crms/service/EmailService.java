package com.crms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	@Value("${app.frontend-url}")
	private String frontendUrl;
	
	@Value("${app.base-url}")
	private String baseUrl;
	
	public void sendVerificationEmail(String toEmail, String token) {
		
		String subject = "CRMS Email Verification";
		
		String verificationUrl = baseUrl + "/auth/verify-email?token=" + token;
		
		String message = "Hello,\n\nPlease verify your email by clicking the link below:\n\n"
				+ verificationUrl
				+ "\n\nIf you did not register, please ignore this email.";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(toEmail);
		
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		
		mailSender.send(mailMessage);
	}
	
	public void sendPasswordResetEmail(String toEmail, String token) {
		
		String subject = "CRMS Password Reset";
		
		String resetUrl = frontendUrl + "/reset-password?token=" + token;
		
		String message = "Hello, \n\nClick the link below to reset your password: \n\n"
				+resetUrl
				+"\n\nIf you did not request a password reset, please ignore this email.";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(toEmail);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		
		mailSender.send(mailMessage);
		
	}

}
