package com.springboard.backend.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class Oauth2AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {

	@Autowired
	private ClientDetailsService clientdetailsService;
	
	public Oauth2AuthorizationServerConfiguration(BaseClientDetails details,
            AuthenticationConfiguration authenticationConfiguration,
            ObjectProvider<TokenStore> tokenStore,
            ObjectProvider<AccessTokenConverter> tokenConverter,
            AuthorizationServerProperties properties) throws Exception {
		//throws 함수안에서 처리하겠다 =/= throw
		super(details, authenticationConfiguration, tokenStore, tokenConverter, properties);
	}
	
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientdetailsService);
	}
}
