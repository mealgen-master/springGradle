package com.springboard.backend.dto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.springboard.backend.model.UserRole;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode(of = "uid")
public class Users {
	
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
	private List<UserRole> userRoles = new ArrayList<>();
	
}