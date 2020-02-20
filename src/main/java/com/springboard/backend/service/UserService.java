package com.springboard.backend.service;

import org.springframework.stereotype.Service;

import com.springboard.backend.dto.Users;
import com.springboard.backend.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Iterable<Users> selectAllList() {
		return userRepository.findAll();
	}
	
	public void setUserData(Users user) {
		userRepository.save(user);
	}
	
	public void updateUserData(Integer id, String username, String phonenumber, String address, String address2 ) {
		Users user = userRepository.findById(id).get();

		user.setUsername(username);
		user.setPhonenumber(phonenumber);
		user.setAddress(address);
		user.setAddress2(address2);
		
		userRepository.save(user);
	}
	
	public void deleteUserData(Integer id) {
//		Users user = userRepository.findById(id).get();
//		userRepository.delete(user);
		userRepository.deleteById(id);
	}
	
	public Users selectUserData(Integer id) {
		return userRepository.findById(id).get();
	}
}