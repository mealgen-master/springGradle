package com.springboard.backend.controller;

import com.springboard.backend.dto.UsersDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserResourceAssembler implements RepresentationModelAssembler<UsersDTO.Response, EntityModel<UsersDTO.Response>> {

    @Setter
    @Getter
    private String type;

    @SneakyThrows
    @Override
    public EntityModel<UsersDTO.Response> toModel(UsersDTO.Response entity) {
        Link linkbuilder;
        switch(type){
            case "update":
                linkbuilder = linkTo(methodOn(UserController.class).updateUserDTO(entity.getId(),UsersDTO.Update.builder().build())).withRel("update");
                break;
            case "create" :
                linkbuilder = linkTo(methodOn(UserController.class).addUserDTO(UsersDTO.Create.builder().build())).withRel("create");
                break;
            default :
                linkbuilder = linkTo(methodOn(UserController.class).selectUserDTO(entity.getId())).withSelfRel();
        }

        return new EntityModel<>(
                entity,
                linkbuilder
        );
    }
}