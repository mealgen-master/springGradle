package com.springboard.backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

import com.springboard.backend.dto.UsersDTO;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.springboard.backend.dto.Users;
import com.springboard.backend.model.OAuthToken;
import com.springboard.backend.model.OauthClientDetails;
import com.springboard.backend.model.UserRole;
import com.springboard.backend.model.UserRole.Role;
import com.springboard.backend.repository.UserJpaRepository;
import com.springboard.backend.service.UserService;

import io.swagger.annotations.Api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Api(tags = {"유저 API"})
@RestController
//@RequestMapping(path = UserController.REQUEST_BASE_PATH)
//@ControllerAdvice
@RequiredArgsConstructor
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
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	private UserResourceAssembler userResourceAssembler = new UserResourceAssembler();

	@PostMapping(path ="/api/addUserDTO",produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<EntityModel<UsersDTO.Response>> addUserDTO(@RequestBody  @Valid UsersDTO.Create userCreateDto) throws URISyntaxException {
		//        Users users = Users.builder() 1. ModelMapper 를 사용하지 않는 방법
//                .username(userCreateDto.getUsername())
//                .phonenumber(userCreateDto.getPhonenumber())
		//ModelMapper 를 사용하는 방법
//		Users users = modelMapper.map(userCreateDto , Users.class); // 위에 사용하지 않는 방법은 많은 값을 입력한다. //ModelMapper 를 사용하면 이 1줄로 들어온 모든 값을 1세팅 할 수 있다.

		UsersDTO.Response usersDtoResponse = userService.setUserDataDto(userCreateDto);

		userResourceAssembler.setType("create");
		EntityModel<UsersDTO.Response> resource = userResourceAssembler.toModel(usersDtoResponse);

		// RestTemplate 에 MessageConverter 세팅
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new FormHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(converters);

		// parameter 세팅
		// 49.50.165.170:
		//        49.50.165.35:8080
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("client_id", "testClientId");
		map.add("access_token_validity", "36000");
		map.add("authorities", "USER");
//		map.add("authorized_grant_types", "authorization_code,refresh_token");
		map.add("authorized_grant_types", "password refresh_token client_credentials");
		map.add("client_secret", passwordEncoder.encode("testSecret"));
		map.add("refresh_token_validity", "50000");
		map.add("scope", "read,write");
		map.add("web_server_redirect_uri", "http://localhost:8080/oauth/token");
//	    map.add("web_server_redirect_uri", "http://49.50.165.35:8080/oauth2/callback");
		map.add("additional_information", "");
		map.add("autoapprove", "");
		map.add("resource_ids", "");

		// REST API 호출
		System.out.println("------------------ 결과 ------------------");
		String result = restTemplate.postForObject("http://localhost:8080/api/oauthDetailAdd", map, String.class);
		System.out.println(result);
		System.out.println("------------------ ------------------");

		return ResponseEntity.ok(resource);
	}

	@PutMapping(path = "/api/updateDTO/{id}" , produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<EntityModel<UsersDTO.Response>> updateUserDTO (@ApiParam(required = true, example = "1") @PathVariable final Integer id, @RequestBody @Valid UsersDTO.Update userDtoUpdate) throws AccountNotFoundException {
		UsersDTO.Response usersDtoResponse = userService.updateUserDto(id ,userDtoUpdate);
		userResourceAssembler.setType("update");
		EntityModel<UsersDTO.Response> resource = userResourceAssembler.toModel(usersDtoResponse);

		return ResponseEntity.ok(resource);
	}

//	@GetMapping("/api/selectUserDTO/{id}")
	@GetMapping(path = "/api/selectUserDTO/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public  ResponseEntity<EntityModel<UsersDTO.Response>> selectUserDTO(@ApiParam(required = true , example = "1") @PathVariable final  Integer id) {
		UsersDTO.Response userDtoResponse = userService.selectUserDTO(id);
		userResourceAssembler.setType("select");
		EntityModel<UsersDTO.Response> resource = userResourceAssembler.toModel(userDtoResponse);

		return ResponseEntity.ok(resource);
	}


	@DeleteMapping("/api/deleteUserDTO/{id}")
	public ResponseEntity<?> deleteUserDTO(@ApiParam(required = true, example = "1") @PathVariable final Integer id) {
		userService.deleteUserDTO(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/api/addUser")
	private ResponseEntity<?> addUser(
			@RequestParam(name="username") String username,
			@RequestParam(name="phonenumber") String phonenumber,
			@RequestParam(name="address") String address,
			@RequestParam(name="address2") String address2,
			@RequestParam(name="rolename") Role rolename
	) {
		UserRole userRole = new UserRole();
		
		// auth.authenticationProvider(authenticationProvider); 으로 인한 BCryptPasswordEncoder 다시 주
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		
		Users user = new Users();
		user.setUsername(username);
		user.setPhonenumber(passwordEncoder.encode(phonenumber));
		user.setAddress(address);
		user.setAddress2(address2);
		userRole.setRolename(rolename);
		user.setUserRoles(Arrays.asList(userRole));
		
		userService.setUserData(user);
		
		// RestTemplate 에 MessageConverter 세팅
	    List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new FormHttpMessageConverter());
	    converters.add(new StringHttpMessageConverter());
	 
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.setMessageConverters(converters);
	 
	    // parameter 세팅
	    // 49.50.165.170:
        //        49.50.165.35:8080
	    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
	    map.add("client_id", "testClientId");
	    map.add("access_token_validity", "36000");
	    map.add("authorities", "USER");
	    map.add("authorized_grant_types", "authorization_code,refresh_token");
//		map.add("authorized_grant_types", "password ,refresh_token, client_credentials");
	    map.add("client_secret", passwordEncoder.encode("testSecret"));
	    map.add("refresh_token_validity", "50000");
	    map.add("scope", "read,write");
	    map.add("web_server_redirect_uri", "http://localhost:8080/oauth2/callback");
//	    map.add("web_server_redirect_uri", "http://49.50.165.35:8080/oauth2/callback");
	    map.add("additional_information", "");
	    map.add("autoapprove", "");
	    map.add("resource_ids", "");
	    
	    // REST API 호출
	    System.out.println("------------------ 결과 ------------------");
	    String result = restTemplate.postForObject("http://localhost:8080/api/oauthDetailAdd", map, String.class);
	    System.out.println(result);
	    System.out.println("------------------ ------------------");
		
//		return username + " 저장 완료";
	    return ResponseEntity.ok().build();
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
	public Users selectUsers(Integer id) {
//		 System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		//		System.out.print(address2);
		return (Users) userService.selectAllList();
	}
	
	
	@RequestMapping(value = "/api/oauthDetailAdd", method = RequestMethod.POST)
	public String addAuthSetting(@RequestParam(name="client_id") String client_id, @RequestParam(name="resource_ids") String  resource_ids,
			@RequestParam(name="client_secret") String  client_secret,@RequestParam(name="scope") String scope,@RequestParam(name="authorized_grant_types") String authorized_grant_types,
			@RequestParam(name="web_server_redirect_uri") String  web_server_redirect_uri,@RequestParam(name="authorities") String  authorities,
			@RequestParam(name="access_token_validity") Integer  access_token_validity, @RequestParam(name="refresh_token_validity") Integer  refresh_token_validity,
			@RequestParam(name="additional_information") String  additional_information,@RequestParam(name="autoapprove") String autoapprove) {
		OauthClientDetails oauthClientDetails = new OauthClientDetails();
		oauthClientDetails.setClient_id(client_id);
		oauthClientDetails.setResource_ids(resource_ids);
		oauthClientDetails.setClient_secret(client_secret);
		oauthClientDetails.setScope(scope);
		oauthClientDetails.setAuthorized_grant_types(authorized_grant_types);
		oauthClientDetails.setWeb_server_redirect_uri(web_server_redirect_uri);
		oauthClientDetails.setAuthorities(authorities);
		oauthClientDetails.setAccess_token_validity(access_token_validity);
		oauthClientDetails.setRefresh_token_validity(refresh_token_validity);
		oauthClientDetails.setAdditional_information(additional_information);
		oauthClientDetails.setAutoapprove(autoapprove);
		userService.addOauthCilentDetail(oauthClientDetails);
		return " 인증 정보 저장 완료.";
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
         //네이버 클라우드용////
//        params.add("redirect_uri", "http://49.50.165.35:8080/oauth2/callback");
         //////////////
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        System.out.println("강진희가 드디어 잡았다22222222.");
        
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/token", request, String.class);
        //네이버 클라우드용////
//        ResponseEntity<String> response = restTemplate.postForEntity("http://49.50.165.35:8080/oauth/token", request, String.class);
        // 49.50.165.170:
        //        49.50.165.35:8080
        //////////////
        System.out.println("강진희가 드디어 잡았다.33333333333");
        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), OAuthToken.class);
        }
        System.out.println("강진희가 드디어 잡았다.44444444444444444444");
        return null;
    }
	
	@GetMapping(value = "/oauth2/token/refresh")
	public OAuthToken refreshToken(@RequestParam String refreshToken) {
		String credentials = "testClientId:testSecret";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + encodedCredentials);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/token", request, String.class);
      //네이버 클라우드용////
//        ResponseEntity<String> response = restTemplate.postForEntity("http://49.50.165.35:8080/oauth/token", request, String.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), OAuthToken.class);
        }
        return null;
	}
	
	
	
}
