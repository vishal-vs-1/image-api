package com.mvc.project.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mvc.project.dto.LoginDto;
import com.mvc.project.security.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtService jwtService;
	
	private final AuthenticationManager authManager;
	
	public String login(LoginDto auth) {
		Authentication authenticate = 
				authManager.authenticate(
						new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword())
						);
		if(authenticate.isAuthenticated())
			return jwtService.generateToken(auth.getEmail());
		else
			return "something wrong";
	}
}
