package com.mvc.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.project.entity.User;


public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);
}
