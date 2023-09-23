package com.mvc.project.dto;

import com.mvc.project.entity.User;

import lombok.Data;

@Data
public class RegisterDto {

	private String email;
	
	private String username;
	
	private String password;
	
	public static User registerDtoToUser(RegisterDto dto){
		return User.builder().email(dto.getEmail())							 
							 .username(dto.getUsername())
							 .build();
	}
}
