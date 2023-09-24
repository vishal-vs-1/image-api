package com.mvc.project.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.catalina.webresources.FileResource;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mvc.project.dto.ImageDataDto;
import com.mvc.project.dto.RegisterDto;
import com.mvc.project.entity.Image;
import com.mvc.project.entity.Role;
import com.mvc.project.entity.User;
import com.mvc.project.repository.ImageRepository;
import com.mvc.project.repository.RoleRepository;
import com.mvc.project.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	@Autowired
	private final PasswordEncoder encoder;
	
	private final UserRepository userRepo;
	
	private final ImageRepository imgRepo;
	
	private final JavaMailSender mailSender;
	
	private final RoleRepository roleRepo;
	
	@Value("${file.path}")
	private String STORAGE_PATH;
	
	@Override
	public void registerUser(RegisterDto dto) {
		System.out.println(dto.getPassword());
		var u = RegisterDto.registerDtoToUser(dto);
		u.setPassword(encoder.encode(dto.getPassword()));
		u.setRoles(new HashSet<>());
		
		Optional<Role> op = roleRepo.findById(1);
		if(op.isEmpty()) {
			Role role = new Role(1, "user");
			roleRepo.save(role);
		}
		
		u.getRoles().add(roleRepo.findById(1).get());
		
		userRepo.save(u);
	}

	@Override
	public void saveImage(MultipartFile image, String email) throws IOException {
		
		String fileName = image.getOriginalFilename();
		String ext = fileName.substring(fileName.lastIndexOf("."));
		String newName = UUID.randomUUID().toString().concat(ext);
		String path = STORAGE_PATH + newName;
		log.info(STORAGE_PATH);
		log.info(path);
		
		File file = new File(STORAGE_PATH);
		if(!file.exists())
			file.mkdir();
		
		Files.copy(image.getInputStream(), Paths.get(path));
				
		imgRepo.save(Image.builder().name(newName).user(this.getCurrentUser(email)).build());
	}
	
	@Override
	public String getRandomImage() throws Exception {
		
		Optional<Image> op = imgRepo.generateRandomImage();
		Image image = op.orElseThrow(() -> new Exception("No images in databse"));
			
		String path = STORAGE_PATH + image.getName();
		log.info(path);
		
		return path;
	}
	
	@Override
	public List<ImageDataDto> getAllUserImages(String email) throws Exception{
		List<Image> list = imgRepo.findImagesByUserEmail(email);
		List<ImageDataDto> res = new ArrayList<>();
		
		if(list.isEmpty())
			throw new Exception("No images");
		
		list.stream().forEach(c->{
			File file = new File(STORAGE_PATH+c.getName());			
			ImageDataDto img = null;
			try {
				img = new ImageDataDto(c.getImageId(), Files.readAllBytes(file.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			res.add(img);
		});
		return res;
		
	}
	
	@Override
	public void deleteImages(int id, String email) throws Exception {
		if(!imgRepo.existsByUserEmailAndId(email,id))
			throw new Exception("");
		
		log.info("" + id);
		var img = imgRepo.findById(id).get();
		File file = new File(STORAGE_PATH + img.getName());
		Files.delete(file.toPath());
		
		imgRepo.delete(img);
	}

	@Override
	public void mailRandomImage(String userMail, String receiverMail) throws Exception {
		String img = this.getRandomImage();
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		var helper = new MimeMessageHelper(mimeMessage, true);
		
		helper.setTo(receiverMail);
		helper.setText("from" + userMail);
		helper.addAttachment(img, new FileSystemResource(new File(img)));
		
		mailSender.send(mimeMessage);
	}
	
	
	
	private User getCurrentUser(String email) {		
		return userRepo.findByEmail(email).get();
	}

	
}