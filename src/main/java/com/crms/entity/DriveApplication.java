package com.crms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriveApplication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private User student;
	
	@ManyToOne
	@JoinColumn(name = "drive_id", nullable = false)
	private PlacementDrive drive;
	
	private LocalDateTime appliedAt;

}
