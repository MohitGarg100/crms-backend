package com.crms.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.crms.entity.User;
import com.crms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public User registerUser(User user) {
		
		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
		
		if (existingUser.isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		return userRepository.save(user);
	}
	
	public User getUserByEmail(String email) {
		
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

}
