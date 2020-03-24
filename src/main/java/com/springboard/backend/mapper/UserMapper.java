package com.springboard.backend.mapper;

import com.springboard.backend.dto.Users;
import com.springboard.backend.dto.UsersDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UsersDTO.Response , Users>{


}
