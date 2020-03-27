package com.springboard.backend.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboard.backend.model.OauthClientDetails;
import com.springboard.backend.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "uid")
public class Users implements UserDetails {
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	@Column
	private String username;
	
	@Column
	private String phonenumber;
	
	@Column
	private String address;
	
	@Column
	private String address2;
	
	public boolean isAddress(String address) {
		return address == "우리집";
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	// FetchType = 로딩 타입을 Eager 또는 lazy로 지정 == 즉시로딩/ 지연로딩
	@JoinColumn(name="user_id")
	@ElementCollection(fetch = FetchType.EAGER)
	private List<UserRole> userRoles = new ArrayList<>();
	
	
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
//	// FetchType = 로딩 타입을 Eager 또는 lazy로 지정 == 즉시로딩/ 지연로딩
//	@JoinColumn(name="client_id")
//	private OauthClientDetails oauthClientDetails;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.userRoles.stream().map(role -> new SimpleGrantedAuthority( "ROLE_" + role.getRolename())).collect(Collectors.toSet());
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Override
	public String getUsername() {
		return this.username;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Override
	public String getPassword() {
		return this.phonenumber;
	}
	
}