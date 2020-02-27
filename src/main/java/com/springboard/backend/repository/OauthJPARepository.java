package com.springboard.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboard.backend.model.OauthClientDetails;


public interface OauthJPARepository extends JpaRepository<OauthClientDetails, Integer> {

}