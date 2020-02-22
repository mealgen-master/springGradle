package com.springboard.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.springboard.backend.service.UserDetailsServiceImpl;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public DefaultAccessTokenConverter accessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("testClientId").secret(passwordEncoder.encode("testSecret"))
		// inMemory에 저장된 값과 
				.redirectUris("http://localhost:8080/oauth2/callback").authorizedGrantTypes("authorization_code")
				.scopes("read", "write").accessTokenValiditySeconds(30000);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())
				.authenticationManager(authenticationManager)
				.userDetailsService(userDetailsServiceImpl);
	}
}
