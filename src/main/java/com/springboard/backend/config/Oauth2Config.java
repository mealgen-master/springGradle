package com.springboard.backend.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class Oauth2Config {
	
	@Bean
	public TokenStore jdbcTokenStore(DataSource datasource) {
		return new JdbcTokenStore(datasource);
		// jdbcTokenStore.java => jdbcTemplate.java에 상속메소드를 통해 dataSource 정보를 저장
	}
	
	public JdbcClientDetailsService jdbcClientDetailsService(DataSource datasource) {
		return new JdbcClientDetailsService(datasource);
	}
}
