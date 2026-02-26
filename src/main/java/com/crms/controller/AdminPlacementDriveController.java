package com.crms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crms.entity.DriveApplication;
import com.crms.entity.PlacementDrive;
import com.crms.service.PlacementDriveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/drives")
@RequiredArgsConstructor
public class AdminPlacementDriveController {
	
	private final PlacementDriveService placementDriveService;
	
	@PostMapping("/{adminId}")
	public ResponseEntity<PlacementDrive> createDrive(
			@PathVariable Long adminId,
			@Valid @RequestBody PlacementDrive drive) {
		
		PlacementDrive savedDrive = placementDriveService.createDrive(drive, adminId);
		
		return ResponseEntity.ok(savedDrive);
	}
	
	@GetMapping("/{driveId}/applicants/{adminId}")
	public ResponseEntity<List<DriveApplication>> getApplicants(
			@PathVariable Long driveId,
			@PathVariable Long adminId) {
		
		List<DriveApplication> applications = placementDriveService.getApplicationsForDrive(driveId, adminId);
		
		return ResponseEntity.ok(applications);
	}
	
	@PutMapping("/{driveId}/close/{adminId}")
	public ResponseEntity<String> closeDrive(
			@PathVariable Long driveId,
			@PathVariable Long adminId) {
		
		String response = placementDriveService.closeDrive(driveId, adminId);
		
		return ResponseEntity.ok(response);
	}
	

}
