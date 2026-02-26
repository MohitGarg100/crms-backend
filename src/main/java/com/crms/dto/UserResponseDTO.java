package com.crms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
	
	private Long id;
	private String fullName;
	private String email;

}
