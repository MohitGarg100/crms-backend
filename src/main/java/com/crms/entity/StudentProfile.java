package com.crms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	@Column(nullable = false)
	private String fullName;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private String course;
	
	@Column(nullable = false)
	private String stream;
	
	@Column(nullable = false)
	private Double tenthPercentage;
	
	@Column(nullable = false)
	private Double twelfthPercentage;
	
	@Column(nullable = false)
	private Double graduationCgpa;
	
	// Only optional field
	private Double postGraduationCgpa;
	
	@Column(nullable = false)
	private Boolean hasActiveBacklog;
	
	@Column(nullable = false)
	private Integer numberOfActiveBacklogs;
	
	@Column(nullable = false)
	private String mobile;
	
	@Column(nullable = false)
	private String resumeUrl;

}
