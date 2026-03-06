package com.crms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crms.entity.DriveApplication;
import com.crms.entity.PlacementDrive;
import com.crms.security.CustomUserDetails;
import com.crms.service.PlacementDriveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/drives")
@RequiredArgsConstructor
public class AdminPlacementDriveController {
	
	private final PlacementDriveService placementDriveService;
	
	@PostMapping
	public ResponseEntity<PlacementDrive> createDrive(
			@Valid @RequestBody PlacementDrive drive,
			Authentication authentication) {
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long adminId = userDetails.getId();
		
		PlacementDrive savedDrive = placementDriveService.createDrive(drive, adminId);
		
		return ResponseEntity.ok(savedDrive);
	}
	
	@GetMapping("/open")
	public ResponseEntity<List<PlacementDrive>> getOpenDrives() {
		
		List<PlacementDrive> drives = placementDriveService.getOpenDrives();
		
		return ResponseEntity.ok(drives);
	}
	
	@GetMapping("/{driveId}/applicants")
	public ResponseEntity<List<DriveApplication>> getApplicants(
			@PathVariable Long driveId,
			Authentication authentication) {
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long adminId = userDetails.getId();
		
		List<DriveApplication> applications = placementDriveService.getApplicationsForDrive(driveId, adminId);
		
		return ResponseEntity.ok(applications);
	}
	
	@PutMapping("/{driveId}/close")
	public ResponseEntity<String> closeDrive(
			@PathVariable Long driveId,
			Authentication authentication) {
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long adminId = userDetails.getId();
		
		String response = placementDriveService.closeDrive(driveId, adminId);
		
		return ResponseEntity.ok(response);
	}
	

}
