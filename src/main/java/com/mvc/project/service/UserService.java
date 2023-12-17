package com.mvc.project.service;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mvc.project.dto.ImageDataDto;
import com.mvc.project.dto.RegisterDto;

public interface UserService {

	public void registerUser(RegisterDto dto);
	
	public void saveImage(MultipartFile image, String username) throws IOException ;
	
	public String getRandomImage() throws Exception;
	
	public List<String> getAllUserImages(String email) throws Exception;
	
	public void deleteImages(int id, String email) throws Exception;

	public void mailRandomImage(String name, String receiverMail) throws Exception;
	
	public String getImageById(int id) throws Exception;
	
}
