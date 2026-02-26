package com.crms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
	
	private String fullName;
	private String email;
	private String password;

}
