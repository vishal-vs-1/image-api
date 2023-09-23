package com.mvc.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.project.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}
