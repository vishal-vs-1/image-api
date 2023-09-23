package com.mvc.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.project.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer>{
	
	@Query(value = "select * from images order by rand() limit 1", nativeQuery = true)
	Optional<Image> generateRandomImage();
	
	@Query("select i from Image i where i.user.email = :email")
    List<Image> findImagesByUserEmail(@Param("email") String email);
	
	@Query(	  "SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false"
			+ " END FROM Image i WHERE i.user.email = :email AND i.id = :id")
	boolean existsByUserEmailAndId(String email, int id);
}
