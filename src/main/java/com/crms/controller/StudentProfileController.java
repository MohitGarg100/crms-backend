package com.crms.controller;

import org.springframework.web.bind.annotation.*;

import com.crms.dto.StudentProfileRequestDTO;
import com.crms.entity.StudentProfile;
import com.crms.service.StudentProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentProfileController {
	
	private final StudentProfileService studentProfileService;
	
	@PostMapping("/{userId}/profile")
	public StudentProfile createProfile(
			@PathVariable Long userId,
			@RequestBody StudentProfileRequestDTO request) {
		
		return studentProfileService.createProfile(userId, request);
	}

}
