package com.crms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.crms.entity.Role;
import com.crms.entity.User;
import com.crms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) {
		
		if (userRepository.findByEmail("admin@crms.com").isEmpty()) {
			
			User admin = new User();
			admin.setUid("ADMIN001");
			admin.setEmail("admin@crms.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole(Role.ADMIN);
			
			userRepository.save(admin);
			
			System.out.println("Default admin created");
			
		}
	}

}
