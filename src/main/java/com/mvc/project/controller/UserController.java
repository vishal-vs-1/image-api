package com.mvc.project.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mvc.project.dto.ImageDataDto;
import com.mvc.project.dto.RegisterDto;
import com.mvc.project.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin()
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

	private UserService service;

	private UserDetailsService uds;

	@GetMapping("/details")
	public UserDetails ggg(Principal principal) {

		return uds.loadUserByUsername(principal.getName());
	}

	@PostMapping("/register")
	public String registerUser(@RequestBody RegisterDto dto) {
		service.registerUser(dto);
		return "user successfully registered";
	}

	@PostMapping("/upload")
	public String uploadImage(@RequestParam MultipartFile image, Authentication auth) throws IOException {
		service.saveImage(image, auth.getName());
		return "image successfully uploaded";
	}

	@GetMapping("/getImagesData")
	public List<String> getAllUserImages(Authentication auth) throws Exception {
		return service.getAllUserImages(auth.getName());
	}

	@DeleteMapping("/deleteImage/{id}")
	public String deleteImage(Authentication auth, @PathVariable int id) throws Exception {
		service.deleteImages(id, auth.getName());
		return "image deleted succefully";
	}

	@PostMapping("/sendMail/{receiverMail}")
	public String mailRandomImage(Authentication auth, @PathVariable String receiverMail) throws Exception {
		service.mailRandomImage(auth.getName(), receiverMail);
		return "image successfully mailed";
	}
}
