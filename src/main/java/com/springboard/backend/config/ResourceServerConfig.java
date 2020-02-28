package com.springboard.backend.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import io.micrometer.core.instrument.util.IOUtils;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${security.oauth2.jwt.signkey}")
	private String signKey;
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
     * jwt converter - signKey 공유 방식
     */
//	@Bean
//	public JwtAccessTokenConverter accessTokenConverter() {
//		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		converter.setVerifierKey(signKey);
//		converter.setSigningKey(signKey);
//		return converter;
//	}

	/**
     * jwt converter - 비대칭 키 sign
     */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    	KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource("src/main/resources/oauth2jwt.jks"), "oauth2jwtpass".toCharArray());
    	converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2jwt"));
		return converter;
	}
	
    /**
     * Resource ID 설정
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.headers().frameOptions().disable();
        http.authorizeRequests()
        	.antMatchers("/oauth/**", "/oauth/token", "/oauth2/callback**", "/h2-console/*").permitAll()
            .antMatchers("/api/users").access("#oauth2.hasScope('read')")
            .anyRequest().authenticated();
//        http
//            .anonymous()
//                .and()
//            .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
//                .anyRequest().authenticated()
//                .and()
//            .exceptionHandling()
//                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
            // 인증이 안되거나, 권한이없는경우 예외가 발생하며 OAuth2AccessDeniedHandler 가 403 응답을 내보낸다.
        ;
    }
}
