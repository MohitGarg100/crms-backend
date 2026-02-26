package com.crms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "placement_drives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementDrive {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@NotBlank
	private String companyName;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DriveType driveType;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DriveDateType driveDateType;
	
	private LocalDate driveStartDate;
	
	private LocalDate driveEndDate;
	
	@NotNull
	private String driveDateNote;
	
	private String companyWebsite;
	
	private String linkedinPage;
	
	@Column(nullable = false)
	@NotBlank
	private String streamRequired;
	
	@Column(length = 1000, nullable = false)
	@NotBlank
	private String eligibilityCriteria;
	
	@Column(nullable = false)
	@NotBlank
	private String batch;
	
	@Column(nullable = false)
	@NotBlank
	private String position;
	
	@Column(length = 1000, nullable = false)
	@NotBlank
	private String jobProfile;
	
	@Column(nullable = false)
	@NotBlank
	private String jobLocation;
	
	private LocalDate dateOfJoining;
	
	@Column(nullable = false)
	@NotBlank
	private String payPackage;
	
	@Column(nullable = false)
	@NotBlank
	private String bondOrFee;
	
	@Column(length = 2000, nullable = false)
	@NotBlank
	private String placementProcess;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DriveStatus status;

}
