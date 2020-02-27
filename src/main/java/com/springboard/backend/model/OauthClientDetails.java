package com.springboard.backend.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OauthClientDetails {
	
	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique = true)
	private String client_id;
	
	@Column
	private String client_secret; 
	
	@Column
	private String resource_ids; 
	
	@Column
	private String scope; 
	
	@Column
	private String authorized_grant_types; 
	
	@Column
	private String web_server_redirect_uri;
	
	@Column
	private String authorities;
	
	@Column
	private Integer access_token_validity;
	
	@Column
	private Integer refresh_token_validity;
	
	@Column
	private String additional_information;
	
	@Column
	private String autoapprove;

}