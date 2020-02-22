package com.springboard.backend.controller;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.springboard.backend.dto.Users;
import com.springboard.backend.model.OAuthToken;
import com.springboard.backend.model.UserRole;
import com.springboard.backend.model.UserRole.Role;
import com.springboard.backend.repository.UserJpaRepository;
import com.springboard.backend.service.UserService;


@RestController
//@RequestMapping(path = UserController.REQUEST_BASE_PATH)
//@ControllerAdvice
public class UserController {
	
	static final String REQUEST_BASE_PATH = "/api/users";
	
//	@ExceptionHandler(Exception.class) 
//	public String custom() { 
//		return "잘못 파라미터 입력 및 잘못된 url 주소."; 
//	}
	
	@Resource(name="userJpaRepository")
	// 의존성 주입을 name으로 한다.?
	private UserJpaRepository userJpaRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Gson gson;
	
	@Autowired
    private RestTemplate restTemplate;
	
	@GetMapping("/api/address")
	private Iterable<Users> address2(@RequestParam(name="address2") String address) {
		return userService.selectAllList();
	};
	
//	@Autowired
//	PasswordEncoder passwordEncoder;
	
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
	
	@GetMapping(value = "/oauth2/callback")
	public OAuthToken callbackSocial(@RequestParam String code) {
		
        String credentials = "testClientId:testSecret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + encodedCredentials);
 
        System.out.println("강진희가 드디어 잡았다.");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://localhost:8080/oauth2/callback");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        System.out.println("강진희가 드디어 잡았다22222222.");
        
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/token", request, String.class);
        System.out.println("강진희가 드디어 잡았다.33333333333");
//        return response.getBody();
        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), OAuthToken.class);
        }
        System.out.println("강진희가 드디어 잡았다.44444444444444444444");
        return null;
    }
	
	
	
}
