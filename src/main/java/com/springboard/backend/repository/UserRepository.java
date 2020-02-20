package com.springboard.backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.springboard.backend.dto.Users;

public interface UserRepository extends CrudRepository<Users, Integer> {
	
}