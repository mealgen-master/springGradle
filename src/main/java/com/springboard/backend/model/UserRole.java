package com.springboard.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
// Data를 사용하면 Getter-Setter-ToString이 만들어지는데, 왜? 세개를 별도로 사용하는가?
@Entity
public class UserRole {
// 1.new Class() = 함수 안에 2.extends = 클래스 안에 3.UserRole userrole = 클래스 안에서 전역변수로 써야할 때
	// static = UserRole.User을 통해 사용할 수 있다. , final = 최종
	public enum Role {
		ADMIN, USER
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Role rolename;
	
	// Role에 대한 String을 인자로 받기위해서 생성자함수에 String 타입을 지정해준다.
	// 차후에 DB에 저장할 때 String 을 인자로 받기때문에
	public UserRole() {
		
	}
	
	public UserRole(Role rolename) {
		// 소스상에서는 Role( Enum )으로 지정해줘야 함
		this.rolename = rolename;
	}
}
