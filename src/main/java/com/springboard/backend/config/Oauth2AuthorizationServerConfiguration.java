package com.springboard.backend.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.springboard.backend.service.UserDetailsServiceImpl;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

//	@Bean
//	public TokenStore tokenStore() {
//		return new InMemoryTokenStore();
////		return new JwtTokenStore(jwtAccessTokenConverter()); 
//	}

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private DataSource datasource;
	
	 /**
     * jwt converter - signKey 공유 방식
     */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//		return new JwtAccessTokenConverter();
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setVerifierKey(signKey);
		converter.setSigningKey(signKey);
		return converter;	
	}
	
	/**
     * jwt converter - 비대칭 키 sign
     */
//    @Bean
//	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//    	KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource("src/main/resources/oauth2jwt.jks"), "oauth2jwtpass".toCharArray());
////    	KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource("src/main/resources/oauth22jwt.jks"), "oauth2jwtpass".toCharArray());
//    	JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//    	converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2jwt"));
////    	converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth22jwt"));
//    	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//    	return converter;
//    }
    
	@Value("${security.oauth2.jwt.signkey}")
	private String signKey;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(datasource).passwordEncoder(passwordEncoder);
		
//		System.out.print(String.format("Client Secret  : %s ", passwordEncoder.encode("testSecret")));
//		clients.inMemory().withClient("testClientId").secret(passwordEncoder.encode("testSecret"))
//		// inMemory에 저장된 값과 
//				.redirectUris("http://localhost:8080/oauth2/callback").authorizedGrantTypes("authorization_code")
//				.scopes("read", "write").accessTokenValiditySeconds(30000);
		//네이버 클라우드 용.
        // 49.50.165.170:
        //        49.50.165.35:8080
//		clients.inMemory().withClient("testClientId").secret(passwordEncoder.encode("testSecret"))
//		// inMemory에 저장된 값과 
//				.redirectUris("http://49.50.165.35:8080/oauth2/callback").authorizedGrantTypes("authorization_code")
//				.scopes("read", "write").accessTokenValiditySeconds(30000);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		Oauth2Config oauth2Config = new Oauth2Config();
		
		endpoints.accessTokenConverter(jwtAccessTokenConverter()).userDetailsService(userDetailsServiceImpl);
		/************** Jwt 토큰 발급 ***************/
//		endpoints.accessTokenConverter(jwtAccessTokenConverter());
		/********************************************/
//		endpoints.tokenStore(new JdbcTokenStore(datasource));
//				.authenticationManager(authenticationManager)
//				.userDetailsService(userDetailsServiceImpl);
		
		/************** Access 토큰 발급 ***************/
//		endpoints.tokenStore(tokenStore())
//		.authenticationManager(authenticationManager)
//		.userDetailsService(userDetailsServiceImpl);
		/********************************************/
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	    security.tokenKeyAccess("permitAll()")
	            .checkTokenAccess("isAuthenticated()") //allow check token
	            .allowFormAuthenticationForClients();
	}
}
