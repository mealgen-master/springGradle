package com.springboard.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.springboard.backend.Application;
import com.springboard.backend.dto.Users;
import com.springboard.backend.model.UserRole;
import com.springboard.backend.model.UserRole.Role;
import com.springboard.backend.service.UserService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class DemoApplicationTests {

	private MockMvc mockmvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private FilterChainProxy springFilterChainProxy;
	
	@Autowired
	UserService userService;
	
	@Before
	public void setUp() {
		mockmvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(this.springFilterChainProxy).build();
	}
	
	private String getBearerToken () throws Exception {
        return "Bearer " + getToken();
    }

    private String getToken () throws Exception {
        final String USER_NAME = "puppee9@gmail.com";
        final String PASSWORD = "june";
        // httpBasic 메서드를 사용하여 basicOauth 헤더를 만듬
        final String CLIENT_ID = "a05e1610-6c6c-4b52-9eea-b7a5827179f1";
        final String CLIENT_SECRET = "8e4dd445-13c1-4c23-abb1-e939be0d51ec";

        // given
        List<UserRole> roles = new ArrayList<>();
        UserRole userRole = new UserRole();
        userRole.setRolename(Role.USER);
        roles.add(userRole);
//        roles.add(UserRole.USER);
        
        Users account = Users.builder()
                .username(USER_NAME)
                .phonenumber(PASSWORD)
                .userRoles(roles)
                .build();
        userService.setUserData(account);

        ResultActions perform = this.mockmvc.perform(post("/oauth/token")
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)) // httpBasic 사용시 test dependency 필요
                .param("username", USER_NAME)
                .param("password", PASSWORD)
                .param("grant_type", "password")
        );
        MockHttpServletResponse response = perform.andReturn().getResponse();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        String contentAsString = response.getContentAsString();
        Map<String, Object> parseMap = parser.parseMap(contentAsString);
        return parseMap.get("access_token").toString();
    }

	
	@Test
	public void codeMatchingSuccess() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("강진희도전", "client_credentials");
		System.out.println("강진희 가 감지했다 ");
//		ResultActions result = mockmvc.perform(get("/api/selectUser").params(params).with(httpBasic("open_api_key2","waug_secret2"))
		ResultActions result = mockmvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8)
                .content(eventJsonString)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken()));
//				.accept("application/json;charset=UTF-8")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
		System.out.println("강진희 가 감지했다 22222222222222222" + result);
		String resultString = result.andReturn().getResponse().getContentAsString();
		System.out.println("강진희 가 감지했다 33333333333333333333" + resultString);
		JacksonJsonParser jsonParser = new JacksonJsonParser();
        String resultToken = jsonParser.parseMap(resultString).get("access_token").toString();
        System.out.println("강진희 가 감지했다 44444444444444444444444444444");
        System.out.println(resultToken);
	}
	
	@Test
    public void codeMatchingFailure() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");

        ResultActions result
                = mockmvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("open_api_key_not","waug_secret_not"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isUnauthorized());
    }

}
