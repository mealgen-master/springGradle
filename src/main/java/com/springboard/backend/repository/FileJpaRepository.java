package com.springboard.backend.repository;

import com.springboard.backend.model.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<AttachFile, Long> {

}
