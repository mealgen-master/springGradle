package com.springboard.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springboard.backend.dto.Users;
import com.springboard.backend.model.UserRole;
import com.springboard.backend.model.UserRole.Role;
import com.springboard.backend.repository.UserJpaRepository;
import com.springboard.backend.service.UserService;

import lombok.Setter;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
//@RequestMapping(path = UserController.REQUEST_BASE_PATH)
@ControllerAdvice
public class UserController {
	
	static final String REQUEST_BASE_PATH = "/api/users";
	
	@ExceptionHandler(Exception.class) 
	public String custom() { 
		return "잘못 파라미터 입력 및 잘못된 url 주소."; 
	}
	
	@Resource(name="userJpaRepository")
	// 의존성 주입을 name으로 한다.?
	private UserJpaRepository userJpaRepository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/api/address")
	private Iterable<Users> address2(@RequestParam(name="address2") String address) {
		return userService.selectAllList();
	};
	
	@GetMapping("/api/addUser")
	private String addUser(
			@RequestParam(name="username") String username,
			@RequestParam(name="phonenumber") String phonenumber,
			@RequestParam(name="address") String address,
			@RequestParam(name="address2") String address2,
			@RequestParam(name="rolename") Role rolename
	) {
		UserRole userRole = new UserRole();
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		Users user = new Users();
		user.setUsername(username);
		user.setPhonenumber(passwordEncoder.encode(phonenumber));
		user.setAddress(address);
		user.setAddress2(address2);
		userRole.setRolename(rolename);
		user.setUserRoles(Arrays.asList(userRole));
		
		userService.setUserData(user);
		
		return username + " 저장 완료";
	}
	
	@GetMapping("/api/updateUser")
	private String updateUser(
			@RequestParam(name="id") Integer id,
			@RequestParam(name="username") String user,
			@RequestParam(name="phonenumber") String phonenumber,
			@RequestParam(name="address") String address,
			@RequestParam(name="address2") String address2
	) {
		 userService.updateUserData(id, user, phonenumber, address, address2);
		 return user + " 수정 완료";
	}
	
	@GetMapping("/api/deleteUser")
	private String deleteUser(@RequestParam(name="id") Integer id) {
		userService.deleteUserData(id);
		return id + "번째 삭제 완료";
	}

	@GetMapping("/api/selectUser")
	private Users selectUser(@RequestParam(name="id") int id) {
		Users  users = new Users();
		try {
			users = userService.selectUserData(id);
			return  users;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("number format exception");  
		}
		
	}
	
	// DB에 address 필드 추가 후 영향성평가? = 어디어디 소스를 고쳐야하는지 파악 && 파라미터로 받아 값 뿌리기
	@GetMapping("/api/users")
	public Iterable<Users> selectUsers() {
		// System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		//		System.out.print(address2);
		return userService.selectAllList();
	}
	
}
