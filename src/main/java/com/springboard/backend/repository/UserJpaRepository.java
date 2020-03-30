package com.springboard.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.springboard.backend.dto.Users;



public interface UserJpaRepository extends JpaRepository<Users, Integer> {
	 Optional<Users> findByUsername(String username);
	// Users 객체가 들어오거나 Null 이거나
//	Users findByUsername(String username);
}