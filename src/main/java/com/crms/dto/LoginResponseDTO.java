package com.crms.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class LoginResponseDTO {
	
	private Long userId;
	private String uid;
	private String email;
	private String role;
	private boolean profileCreated;
	
	private String token;

}
