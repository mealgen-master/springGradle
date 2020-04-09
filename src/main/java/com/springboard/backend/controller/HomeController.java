package com.springboard.backend.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.springboard.backend.model.Greeting;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HomeController {

    @GetMapping("/")
    public RepresentationModel home() {
        RepresentationModel resourceSupport = new RepresentationModel();
        resourceSupport.add(linkTo(methodOn(UserController.class).selectUsers()).withRel("users"));
        resourceSupport.add(linkTo(methodOn(GreetingController.class).greeting("username")).withRel("greeting"));
        resourceSupport.add(linkTo(FileController.class).withRel("file"));
        return resourceSupport;
    }
}