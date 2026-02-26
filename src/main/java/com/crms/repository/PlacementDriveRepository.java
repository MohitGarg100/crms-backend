package com.crms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crms.entity.DriveStatus;
import com.crms.entity.PlacementDrive;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
		
	List<PlacementDrive> findByStatus(DriveStatus status);
	
	List<PlacementDrive> findByBatch(String batch);
}
