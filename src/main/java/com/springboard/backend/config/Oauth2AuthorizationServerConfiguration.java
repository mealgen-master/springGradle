package com.springboard.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.springboard.backend.model.UserRole.Role;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Bean
	public DefaultAccessTokenConverter accessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("testClientId").secret("testSecret")
				.redirectUris("http://localhost:8080/oauth2/callback").authorizedGrantTypes("authorization_code")
				.scopes("read", "write").accessTokenValiditySeconds(30000);
	}
}
