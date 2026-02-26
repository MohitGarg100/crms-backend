package com.crms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileRequestDTO {
	
	private String fullName;
	private String gender;
	private String course;
	private String stream;
	private Double tenthPercentage;
	private Double twelfthPercentage;
	private Double graduationCgpa;
	private Double postGraduationCgpa;
	private Boolean hasActiveBacklog;
	private Integer numberOfActiveBacklogs;
	private String mobile;
	private String resumeUrl;

}
