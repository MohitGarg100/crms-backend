package com.crms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crms.entity.PlacementDrive;
import com.crms.service.PlacementDriveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students/drives")
@RequiredArgsConstructor
public class StudentPlacementDriveController {
	
	private final PlacementDriveService placementDriveService;
	
	@GetMapping("/open")
	public ResponseEntity<List<PlacementDrive>> getOpenDrives() {
		
		List<PlacementDrive> drives = placementDriveService.getOpenDrives();
		
		return ResponseEntity.ok(drives);
	}
	
	@PostMapping("/{driveId}/apply/{studentId}")
	public ResponseEntity<String> applyToDrive(
			@PathVariable Long driveId,
			@PathVariable Long studentId) {
		
		String response = placementDriveService.applyToDrive(studentId, driveId);
		
		return ResponseEntity.ok(response);
	}

}
