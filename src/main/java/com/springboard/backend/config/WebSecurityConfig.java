package com.springboard.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// 인증매니저빌더?
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	
	
//	@Bean
//    public PasswordEncoder getEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
	
	@Autowired
    private CustomAuthenticationProvider authenticationProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.httpBasic();
		
//		http.authorizeRequests()
//			.antMatchers("/api/**").authenticated()
//			.anyRequest().permitAll();
		
//				.csrf().disable()
//		        .headers().frameOptions().disable()
//		        .and()
//		        .authorizeRequests().antMatchers("/oauth/**", "/oauth/token", "/oauth2/callback", "/h2-console/*").permitAll()
//		        .and()
//		        .formLogin().and()
//		        .httpBasic();
		
//			.authorizeRequests()
//			.antMatchers("/oauth/**", "/oauth2/callback", "/h2-console/*", "/api/addUser**").permitAll()
//			.and()
//			.formLogin();
			
		
		
//		http
//			.formLogin()
//			.defaultSuccessUrl("/")
//			.failureUrl("/api/update");
		
	} 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	

//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
		
		auth.authenticationProvider(authenticationProvider);
		
//		auth.inMemoryAuthentication()
//			.withUser("user").password("{noop}password").roles("USER")
//			.and()
//			.withUser("admin").password("{noop}password").roles("ADMIN");
	}
}
