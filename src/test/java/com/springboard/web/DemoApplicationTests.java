package com.springboard.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.springboard.backend.Application;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
class DemoApplicationTests {

	private MockMvc mockmvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private FilterChainProxy springFilterChainProxy;
	
	@Before
	public void setUp() {
		mockmvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(this.springFilterChainProxy).build();
	}
	
	@Test
	public void codeMatchingSuccess() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "client_credentials");
		
		ResultActions result = mockmvc.perform(post("/oauth/token").params(params).with(httpBasic("open_api_key2","waug_secret2"))
				.accept("application/json;charset=UTF-8")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
		
		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
        String resultToken = jsonParser.parseMap(resultString).get("access_token").toString();

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

	private RequestPostProcessor httpBasic(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

}
