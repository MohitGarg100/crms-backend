package com.crms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crms.dto.LoginRequestDTO;
import com.crms.dto.LoginResponseDTO;
import com.crms.entity.EmailVerificationToken;
import com.crms.entity.PasswordResetToken;
import com.crms.entity.Role;
import com.crms.entity.User;
import com.crms.repository.EmailVerificationTokenRepository;
import com.crms.repository.PasswordResetTokenRepository;
import com.crms.repository.StudentProfileRepository;
import com.crms.repository.UserRepository;
import com.crms.security.CustomUserDetails;
import com.crms.security.JwtService;
import com.crms.service.EmailService;
import com.crms.util.HashUtil;
import com.crms.util.TokenGenerator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final StudentProfileRepository studentProfileRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	private final EmailVerificationTokenRepository emailVerificationTokenRepository;
	private final EmailService emailService;
	
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Value("${app.frontend-url}")
	private String frontendUrl;
	
	@PostMapping("/register")
	public String register(@RequestBody User user) {
		
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		if (userRepository.findByUid(user.getUid()).isPresent()) {
			throw new RuntimeException("UID already exists");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.STUDENT);
		user.setEmailVerified(false);
		
		User savedUser = userRepository.save(user);
		
		String token = TokenGenerator.generateToken();
		String tokenHash = HashUtil.sha256(token);
		
		EmailVerificationToken verificationToken = EmailVerificationToken.builder()
				.token(tokenHash)
				.user(savedUser)
				.expiryDate(LocalDateTime.now().plusHours(24))
				.build();
		
		emailVerificationTokenRepository.save(verificationToken);
		
				emailService.sendVerificationEmail(savedUser.getEmail(), token);
			
		
		return "User registered successfully. Please verify your email.";
	}
	
	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getIdentifier(),
						request.getPassword()
						)
				);
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		String token = jwtService.generateToken(userDetails.getUsername());
		
		User user = userRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		if (!user.isEmailVerified()) {
			throw new RuntimeException("Please verify your email before logging in.");
		}
		
		boolean profileExists = false;
		
		if (user.getRole() == Role.STUDENT) {
			profileExists = studentProfileRepository.existsByUser(user);
		}
		
		return LoginResponseDTO.builder()
				.userId(user.getId())
				.uid(user.getUid())
				.email(user.getEmail())
				.role(user.getRole().name())
				.profileCreated(profileExists)
				.token(token)
				.build();
	}
	
	@GetMapping("/verify-email")
	public void verifyEmail(@RequestParam String token, HttpServletResponse response) throws IOException {
		
		String tokenHash = HashUtil.sha256(token);
		
		EmailVerificationToken verificationToken = emailVerificationTokenRepository
				.findByToken(tokenHash)
				.orElseThrow(() -> new RuntimeException("Invalid verification token"));
		
		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Verification token expired");
		}
		
		User user = verificationToken.getUser();
		user.setEmailVerified(true);
		
		userRepository.save(user);
		
		emailVerificationTokenRepository.delete(verificationToken);
		
		response.sendRedirect(frontendUrl + "/email-verified");
	}
	
	@Transactional
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestBody Map<String, String> request) {
		
		String email = request.get("email");
		
		userRepository.findByEmail(email).ifPresent(user -> {
		
		passwordResetTokenRepository.deleteByUser(user);
		
		String token = TokenGenerator.generateToken();	
		String tokenHash = HashUtil.sha256(token);
		PasswordResetToken resetToken = PasswordResetToken.builder()
				.token(tokenHash)
				.user(user)
				.expiryDate(LocalDateTime.now().plusHours(1))
				.build();
		
		passwordResetTokenRepository.save(resetToken);
		
				emailService.sendPasswordResetEmail(user.getEmail(), token);
		
		});
		
		return "If the email exists, a reset link has been sent.";
		
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		
		String tokenHash = HashUtil.sha256(token);
		
		PasswordResetToken resetToken = passwordResetTokenRepository
				.findByToken(tokenHash)
				.orElseThrow(() -> new RuntimeException("Invalid reset token"));
		
		if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Reset token expired");
		}
		
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		
		userRepository.save(user);
		
		passwordResetTokenRepository.delete(resetToken);
		
		return "Password reset successful. You can now login.";
	}

}
