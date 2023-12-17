package com.mvc.project.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mvc.project.dto.LoginDto;
import com.mvc.project.security.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtService jwtService;
	
	private final AuthenticationManager authManager;
	
	@PostMapping("/login")
	public String login(@RequestBody LoginDto auth) {
		log.info("in method");
		Authentication authenticate = 
				authManager.authenticate(
						new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword())
						);
		log.info(authenticate.isAuthenticated() + "dd");
		if(authenticate.isAuthenticated())
			return jwtService.generateToken(auth.getEmail());
		else
			return "something wrong";
	}
}
