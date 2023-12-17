package com.mvc.project.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mvc.project.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

	private final UserService userService; 
	
	@GetMapping(value = "/random", produces = {
					MediaType.IMAGE_PNG_VALUE, 
					MediaType.IMAGE_GIF_VALUE, 
					MediaType.IMAGE_JPEG_VALUE
					})
	public void randomImage(HttpServletResponse response) throws Exception {		
		String path = userService.getRandomImage();		
		String ext = path.substring(path.lastIndexOf("."));
		
		File file = new File(path);
		InputStream is = new FileInputStream(file);		
		
		switch(ext) {
			case ".jpeg" : response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			case ".png" : response.setContentType(MediaType.IMAGE_PNG_VALUE);
			case ".gif" : response.setContentType(MediaType.IMAGE_GIF_VALUE);
		}
		
		StreamUtils.copy(is, response.getOutputStream());
	}
	
	@GetMapping(value = "/image/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE,
			MediaType.IMAGE_JPEG_VALUE })
	public void getImageById(HttpServletResponse response, @PathVariable int id) throws Exception {
		String path = userService.getImageById(id);
		String ext = path.substring(path.lastIndexOf("."));

		File file = new File(path);
		InputStream is = new FileInputStream(file);

		switch (ext) {
		case ".jpeg":
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		case ".png":
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
		case ".gif":
			response.setContentType(MediaType.IMAGE_GIF_VALUE);
		}

		StreamUtils.copy(is, response.getOutputStream());
	}
}
