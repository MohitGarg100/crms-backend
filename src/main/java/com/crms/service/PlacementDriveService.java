package com.crms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crms.entity.DriveApplication;
import com.crms.entity.DriveStatus;
import com.crms.entity.PlacementDrive;
import com.crms.entity.Role;
import com.crms.entity.User;
import com.crms.repository.DriveApplicationRepository;
import com.crms.repository.PlacementDriveRepository;
import com.crms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlacementDriveService {
	
	private final PlacementDriveRepository placementDriveRepository;
	private final UserRepository userRepository;
	private final DriveApplicationRepository driveApplicationRepository;
	
	@Transactional
	public PlacementDrive createDrive(PlacementDrive drive, Long adminId) {
		
		Optional<User> userOptional = userRepository.findById(adminId);
		
		if(userOptional.isEmpty()) {
			throw new RuntimeException("Admin not found");
		}
		
		User admin = userOptional.get();
		
		if(admin.getRole() != Role.ADMIN) {
			throw new RuntimeException("Only admin can create drive");
		}
		
		drive.setStatus(DriveStatus.OPEN);
		
		return placementDriveRepository.save(drive);
	}
	
	@Transactional
	public String applyToDrive(Long studentId, Long driveId) {
		
		User student = userRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		
		if (student.getRole() != Role.STUDENT) {
			throw new RuntimeException("Only students can apply");
		}
		
		PlacementDrive drive = placementDriveRepository.findById(driveId)
				.orElseThrow(() -> new RuntimeException("Drive not found"));
		
		if(drive.getStatus() != DriveStatus.OPEN) {
			throw new RuntimeException("Drive is not open");
		}
		
		boolean alreadyApplied = driveApplicationRepository
				.existsByStudentAndDrive(student, drive);
		
		if(alreadyApplied) {
			throw new RuntimeException("Already applied to this Drive");
		}
		
		DriveApplication application = DriveApplication.builder()
				.student(student)
				.drive(drive)
				.appliedAt(LocalDateTime.now())
				.build();
		
		driveApplicationRepository.save(application);
		
		return "Application submitted successfully";
	}
	
	public List<PlacementDrive> getOpenDrives() {
		return placementDriveRepository.findByStatus(DriveStatus.OPEN);
	}
	
	public List<DriveApplication> getApplicationsForDrive(Long driveId, Long adminId) {
		
		User admin = userRepository.findById(adminId)
				.orElseThrow(() -> new RuntimeException("Admin not found"));
		
		if (admin.getRole() != Role.ADMIN) {
			throw new RuntimeException("Only admin can view applicants");
		}
		
		PlacementDrive drive = placementDriveRepository.findById(driveId)
				.orElseThrow(() -> new RuntimeException("Drive not found"));
		
		return driveApplicationRepository.findByDrive(drive);
	}
	
	@Transactional
	public String closeDrive(Long driveId, Long adminId) {
		
		User admin = userRepository.findById(adminId)
				.orElseThrow(() -> new RuntimeException("Admin not found"));
		
		if (admin.getRole() != Role.ADMIN) {
			throw new RuntimeException("Only admin can close drive");
		}
		
		PlacementDrive drive = placementDriveRepository.findById(driveId)
				.orElseThrow(() -> new RuntimeException("Drive not found"));
		
		drive.setStatus(DriveStatus.CLOSED);
		
		return "Drive closed successfully";
	}

}
