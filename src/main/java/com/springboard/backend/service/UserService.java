package com.springboard.backend.service;

import com.springboard.backend.dto.UsersDTO;
import com.springboard.backend.mapper.UserMapper;
import com.springboard.backend.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboard.backend.dto.Users;
import com.springboard.backend.model.OauthClientDetails;
import com.springboard.backend.repository.OauthJPARepository;
import com.springboard.backend.repository.UserRepository;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OauthJPARepository oauthJPARepository;

	@Autowired
	private ModelMapper modelMapper;

	private final UserMapper userMapper;


	private UsersDTO.Response toResponse(Users users) {
		//ModelStruct 사용
		return UserMapper.USER_MAPPER.toDto(users);
//		return userMapper.toDto(users);
		//기존 modelMapper 사용
//		return modelMapper.map(users, UsersDTO.Response.class);
	}

	public UsersDTO.Response setUserDataDto(UsersDTO.Create userCreateDto) {
		UserRole userRole = new UserRole();
		userRole.setRolename(userCreateDto.getRolename());
		Users users = Users.builder().username(userCreateDto.getUsername())
				.phonenumber(passwordEncoder.encode(userCreateDto.getPhonenumber())).address(userCreateDto.getAddress())
				.address2(userCreateDto.getAddress2()).userRoles(Arrays.asList(userRole)).build();
		return toResponse(userRepository.save(users));
	}

	public UsersDTO.Response updateUserDto(final Integer id , UsersDTO.Update dtoData) throws AccountNotFoundException {
		UserRole userRole = new UserRole();
		userRole.setRolename(dtoData.getRolename());
		Users user = userRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id.toString()));;
		user.setPhonenumber(passwordEncoder.encode(dtoData.getPhonenumber()));
		user.setAddress(dtoData.getAddress());
		user.setAddress2(dtoData.getAddress2());
		user.setUsername(dtoData.getUsername());
		user.setUserRoles(new ArrayList<>(Arrays.asList(userRole)));
		return toResponse(userRepository.save(user));
	}

	public UsersDTO.Response selectUserDTO(final  Integer id){
		return toResponse(userRepository.findById(id).get());
	}

	public void deleteUserDTO(final  Integer id) {
		userRepository.deleteById(id);
	}

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
	
	public void addOauthCilentDetail(OauthClientDetails oauthClientDetails) {
		 oauthJPARepository.save(oauthClientDetails);
	}
}