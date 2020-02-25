package com.springboard.backend.config;

import javax.sql.DataSource;

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
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

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

	@Autowired
	private DataSource datasource;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(datasource).passwordEncoder(passwordEncoder);
		
//		System.out.print(String.format("Client Secret  : %s ", passwordEncoder.encode("testSecret")));
//		clients.inMemory().withClient("testClientId").secret(passwordEncoder.encode("testSecret"))
//		// inMemory에 저장된 값과 
//				.redirectUris("http://localhost:8080/oauth2/callback").authorizedGrantTypes("authorization_code")
//				.scopes("read", "write").accessTokenValiditySeconds(30000);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		Oauth2Config oauth2Config = new Oauth2Config();
		endpoints.tokenStore(new JdbcTokenStore(datasource));
//				.authenticationManager(authenticationManager)
//				.userDetailsService(userDetailsServiceImpl);
		
//		endpoints.tokenStore(tokenStore())
//		.authenticationManager(authenticationManager)
//		.userDetailsService(userDetailsServiceImpl);

	}
}
