package com.crms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.crms.dto.StudentProfileRequestDTO;
import com.crms.entity.StudentProfile;
import com.crms.security.CustomUserDetails;
import com.crms.service.StudentProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentProfileController {
	
	private final StudentProfileService studentProfileService;
	
	@PostMapping("/profile")
	public StudentProfile createProfile(
			@RequestBody StudentProfileRequestDTO request,
			Authentication authentication) {
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long userId = userDetails.getId();
		
		return studentProfileService.createProfile(userId, request);
	}

}
