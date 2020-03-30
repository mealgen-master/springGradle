package com.springboard.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboard.backend.dto.Users;
import com.springboard.backend.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserJpaRepository userJpaRepository;
    
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
 
        Users user = userJpaRepository.findByUsername(name).orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
				"Authentication credentials not found exception " + name));

        if (!passwordEncoder.matches(password, user.getPhonenumber())) {
			throw new BadCredentialsException("password is not valid");
		}
 
        return new UsernamePasswordAuthenticationToken(name, password, user.getAuthorities());
    
	}

	@Override
	public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
	}

}
