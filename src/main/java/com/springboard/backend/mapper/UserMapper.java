package com.springboard.backend.mapper;

import com.springboard.backend.dto.Users;
import com.springboard.backend.dto.UsersDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UsersDTO.Response , Users>{

    @Mapping(source = "username", target = "username")
    UsersDTO.Response usersToDto(Users users);

    @Mapping(target = "username", ignore = true)
    Users usersDtoTousers(UsersDTO.Response response);


}
