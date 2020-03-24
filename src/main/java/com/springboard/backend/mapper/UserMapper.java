package com.springboard.backend.mapper;

import com.springboard.backend.dto.Users;
import com.springboard.backend.dto.UsersDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UsersDTO.Response , Users>{

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    //target: dto터 파리미터 ,  source : users 파라미
    @Mapping(source = "username", target = "username")
    UsersDTO.Response usersToDto(Users users);

    // target: users 파라미터
    @Mapping(target = "username", ignore = true)
    Users usersDtoTousers(UsersDTO.Response response);


}
