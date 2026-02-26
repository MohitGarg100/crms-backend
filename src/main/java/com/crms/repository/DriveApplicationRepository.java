package com.crms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crms.entity.DriveApplication;
import com.crms.entity.PlacementDrive;
import com.crms.entity.User;

public interface DriveApplicationRepository extends JpaRepository<DriveApplication, Long>{
	
	boolean existsByStudentAndDrive(User student, PlacementDrive drive);
	
	List<DriveApplication> findByDrive(PlacementDrive drive);
	
	List<DriveApplication> findByStudent(User student);

}
