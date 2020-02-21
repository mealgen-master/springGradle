package com.springboard.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// 인증매니저빌더?
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
// crypto = 암호

import com.springboard.backend.service.UserDetailsServiceImpl;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
//	@Autowired
//	PasswordEncoder passwordEncode;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
//	@Bean
//	PasswordEncoder getEncoder() {
//	    return new BCryptPasswordEncoder();
//	}
	
	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	
	@Bean
    public PasswordEncoder getEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.httpBasic();
		
		http.authorizeRequests()
			.antMatchers("/oauth/**", "/oauth2/callback", "/h2-console/*").permitAll()
			.and()
			.formLogin();
//			.antMatchers("/api/selectUser**").authenticated()
//			.anyRequest().permitAll();

			// .antMatchers("/api/selectUser**").hasRole("ADMIN")
			// .anyRequest().permitAll();
			// Admin권한을 가진 사람이 없을 때 해당 요청이 들어왔을 때 403 -> 권한에러 
		
		
//		http
//			.formLogin()
//			.defaultSuccessUrl("/")
//			.failureUrl("/api/update");
		
	} 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws AuthenticationException {	
			try {
				auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(getEncoder());	
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BadCredentialsException("very bad");
			}	
	}
	
	// protected : 패키지 내에서만 공유
	/*
	 	AuthenticationManagerBuilder 란?
		auth.userDetailsService(userDetailsService) 부분에서 하는 일은?
		DaoAuthenticationConfigurer 는 무엇인가?
	 */
}
