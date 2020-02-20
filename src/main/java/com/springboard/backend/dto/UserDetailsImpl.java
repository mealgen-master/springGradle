package com.springboard.backend.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.springboard.backend.model.UserRole;



public class UserDetailsImpl extends User {
	// User 클래스에 들어있는 내용을 상속받아 사용할 수 있다.
	public UserDetailsImpl (Users users) {
		super(users.getUsername(), users.getPhonenumber(), authorities(users.getUserRoles()));
		// 다른 클래스에서 UserDetailsImpl 을 사용할 때 super()안에 들어간 내용을 가지고있다.
	}
	
	private static Collection<? extends GrantedAuthority> authorities(List<UserRole> roles) {
		// 	<> : 제너릭, ? : 타입을 구별하지 않는다.
		List<GrantedAuthority> authorities = new ArrayList<>(); 
		// List > ArrayList 하위개념
		
		roles.forEach(
			role -> authorities.add(new SimpleGrantedAuthority(role.getRolename().toString()))
		);
		
//		if(users.isAddress(users.getAddress())) {
//			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//		} else {
//			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//		}
		return authorities;
	}
}
