package com.springboard.backend.service;

import org.springframework.stereotype.Service;

import com.springboard.backend.dto.UserDetailsImpl;
import com.springboard.backend.dto.Users;
import com.springboard.backend.repository.UserJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	
	@Autowired
	private UserJpaRepository userJpaRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserJpaRepository jparepo = getUserJapRepository();
//		Users users = jparepo.findByUsername(username);
		Users users = userJpaRepository.findByUsername(username);
		// users의 값을 findByUsername에 넣어서 결과값을 보내준다.
		
		if( users == null ) {
			throw new UsernameNotFoundException(username);
		}
		UserDetailsImpl userDetailImpl = new UserDetailsImpl(users);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println(userDetailImpl);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		return userDetailImpl;
	}
}
