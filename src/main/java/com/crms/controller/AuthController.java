package com.crms.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crms.dto.LoginRequestDTO;
import com.crms.dto.LoginResponseDTO;
import com.crms.entity.Role;
import com.crms.entity.User;
import com.crms.repository.StudentProfileRepository;
import com.crms.repository.UserRepository;
import com.crms.security.CustomUserDetails;
import com.crms.security.JwtService;

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
		
		userRepository.save(user);
		
		return "User registered successfully";
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

}
