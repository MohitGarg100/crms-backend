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
		
		User admin = userRepository.findByEmail("admin@crms.com").orElse(null);
		
		if(admin == null) {
			
			admin = new User();
			admin.setUid("ADMIN001");
			admin.setEmail("admin@crms.com");
			admin.setRole(Role.ADMIN);
			
			System.out.println("Default admin created");
		}
		
		admin.setEmailVerified(true);
		admin.setPassword(passwordEncoder.encode("Admin@123"));
		
		userRepository.save(admin);
	}

}
