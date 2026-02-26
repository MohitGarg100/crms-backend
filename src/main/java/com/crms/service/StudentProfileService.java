package com.crms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crms.dto.StudentProfileRequestDTO;
import com.crms.entity.Role;
import com.crms.entity.StudentProfile;
import com.crms.entity.User;
import com.crms.repository.StudentProfileRepository;
import com.crms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentProfileService {
	
	private final StudentProfileRepository studentProfileRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public StudentProfile createProfile(Long userId, StudentProfileRequestDTO request) {
		
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		
		if(user.getRole() != Role.STUDENT) {
			throw new RuntimeException("Only students can create profile");
		}
		
		if(studentProfileRepository.existsByUser(user)) {
			throw new RuntimeException("Profile already exists");
		}
		
		StudentProfile profile = StudentProfile.builder()
				.fullName(request.getFullName())
				.gender(request.getGender())
				.course(request.getCourse())
				.stream(request.getStream())
				.tenthPrecentage(request.getTenthPercentage())
				.twelfthPercentage(request.getTwelfthPercentage())
				.graduationCgpa(request.getGraduationCgpa())
				.postGraduationCgpa(request.getPostGraduationCgpa())
				.hasActiveBacklog(request.getHasActiveBacklog())
				.numberOfActiveBacklogs(request.getNumberOfActiveBacklogs())
				.mobile(request.getMobile())
				.resumeUrl(request.getResumeUrl())
				.user(user)
				.build();
		
		return studentProfileRepository.save(profile);
	}

}
